package com.ai.sync_law.law.port;

import com.ai.sync_law.law.LawDetail;
import com.ai.sync_law.law.LawArticles;

public interface LawFetchPort {
    LawArticles fetchLawSearchResults(String query, int page, int display);
    LawDetail fetchLawDetail(String masterId);
}
