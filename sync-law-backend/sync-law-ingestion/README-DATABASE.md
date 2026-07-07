# 로컬 개발 환경 PostgreSQL x pgvector 구축 가이드

본 문서는 sync-law 백엔드 시스템에서 법령 및 판례 데이터의 벡터 임베딩(Vector Embedding) 저장을 위해 로컬 환경에 PostgreSQL과 pgvector 데이터베이스를 Docker로 구축하는 방법에 대해 기술합니다.

## 1. Docker 컨테이너 생성 및 실행

터미널에 아래 명령어를 실행하여 pgvector 확장이 내장된 공식 PostgreSQL 16 이미지를 컨테이너로 생성합니다.

```bash
docker run --name sync-law-postgres \
    -e POSTGRES_USER=sync_law_user \
    -e POSTGRES_PASSWORD=sync_law_password \
    -e POSTGRES_DB=sync_law_db \
    -p 5432:5432 \
    -d pgvector/pgvector:pg16
```

### 명령어 설명
- `--name sync-law-postgres`: 컨테이너 이름을 `sync-law-postgres`로 지정합니다.
- `-e POSTGRES_USER=sync_law_user`: PostgreSQL 사용자 이름을 `sync_law_user`로 지정합니다.
- `-e POSTGRES_PASSWORD=sync_law_password`: PostgreSQL 사용자 비밀번호를 `sync_law_password`로 지정합니다.
- `-e POSTGRES_DB=sync_law_db`: 생성할 데이터베이스 이름을 `sync_law_db`로 지정합니다.
- `-p 5432:5432`: 호스트의 5432 포트를 컨테이너의 5432 포트에 매핑합니다.
- `-d pgvector/pgvector:pg16`: 벡터 유사도 검색 인덱싱 기능이 사전 설치된 베이스 이미지를 컨테이너화 하여 백그라운드에서 실행합니다.


## 2. vector Extension 활성화

컨테이너 실행 후 데이터베이스 내부에서 vector 타입을 인식할 수 있도록 확장 기능을 활성화합니다.

### 2-1. 컨테이너 내부 psql CLI 접속
```bash
docker exec -it sync-law-postgres psql -U sync_law_user -d sync_law_db
```

### 2-2. Extension 활성화 SQL 실행
```sql
CREATE EXTENSION IF NOT EXISTS vector;
```

- CREATE EXTENSION 메시지가 출력되면 vector 확장이 성공적으로 활성화된 것입니다.
- 쉘을 나갈 때는 `\q` 명령어를 입력합니다.

### 2-3. 테이블 스키마 생성 및 임베딩 테스트
```sql
-- 1. 법령 조문 저장 테이블
CREATE TABLE law_article (
    id BIGSERIAL PRIMARY KEY,
    
    -- 메타데이터
    law_id VARCHAR(20) NOT NULL,       -- 법령ID (예: 003057)
    master_id VARCHAR(20) NOT NULL,    -- 법령일련번호 MST (예: 108467)
    law_name VARCHAR(100) NOT NULL,    -- 법령명한글 (예: 근로감독관규정)
    department VARCHAR(50),            -- 소관부처명 (예: 고용노동부)
    enforce_date VARCHAR(10),          -- 시행일자 (예: 20101027)
    
    -- 조문 데이터 (RAG 파싱 본문)
    article_no VARCHAR(20) NOT NULL,   -- 조문번호 (예: 제1조, 제34조)
    article_title VARCHAR(200),        -- 조문제목 (예: 퇴직급여제도)
    article_content TEXT NOT NULL,     -- 조문내용 및 항/호 전체 텍스트
    
    -- RAG 검색용 벡터 (Bedrock Titan Embedding v2 1536차원 기준)
    embedding vector(1536),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 판례 저장 테이블

CREATE TABLE precedent
(
    id               BIGSERIAL PRIMARY KEY,

    -- 메타데이터
    precedent_id     VARCHAR(20)  NOT NULL, -- 판례일련번호 (예: 616245)
    case_number      VARCHAR(50)  NOT NULL, -- 사건번호 (예: 2023두54914)
    case_name        VARCHAR(500) NOT NULL, -- 사건명 (예: 부당해고구제재심판정취소...)
    court_name       VARCHAR(50)  NOT NULL, -- 법원명 (예: 대법원)
    case_type_name   VARCHAR(50),           -- 사건종류명 (예: 일반행정)
    judgment_date    VARCHAR(10),           -- 선고일자 (예: 2026.01.29)

    -- 판결 본문 (RAG 파싱 본문)
    judgment_summary TEXT,                  -- 판시사항 및 판결요지 전체
    judgment_reason  TEXT,                  -- 판결 이유 (선택 사항)

    -- RAG 검색용 벡터 (Bedrock Titan Embedding v2 1536차원 기준)
    embedding        vector(1536),

    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2-4. 벡터 인덱스 생성
```sql
-- 법령 조문 테이블 벡터 인덱스 생성
CREATE INDEX idx_law_article_embedding ON law_article USING hnsw (embedding vector_cosine_ops);

-- 판례 테이블 벡터 인덱스 생성
CREATE INDEX idx_precedent_embedding ON precedent USING hnsw (embedding vector_cosine_ops);
```