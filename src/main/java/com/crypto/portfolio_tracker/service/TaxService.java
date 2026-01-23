package com.crypto.portfolio_tracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.crypto.portfolio_tracker.dto.PnlResponse;
import com.crypto.portfolio_tracker.dto.TaxSummaryResponse;

@Service
public class TaxService {

    public List<TaxSummaryResponse> prepareTaxSummary(List<PnlResponse> pnlList) {

        List<TaxSummaryResponse> taxList = new ArrayList<>();

        for (PnlResponse p : pnlList) {
            TaxSummaryResponse t = new TaxSummaryResponse();
            t.setAssetSymbol(p.getAssetSymbol());
            t.setInvestedValue(p.getInvestedValue());
            t.setCurrentValue(p.getCurrentValue());
            t.setUnrealizedPnl(p.getUnrealizedPnl());

            taxList.add(t);
        }

        return taxList;
    }
}
