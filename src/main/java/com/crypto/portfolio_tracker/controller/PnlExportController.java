package com.crypto.portfolio_tracker.controller;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.portfolio_tracker.dto.PnlResponse;
import com.crypto.portfolio_tracker.service.PnlService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/pnl")
public class PnlExportController {

    private final PnlService pnlService;

    public PnlExportController(PnlService pnlService) {
        this.pnlService = pnlService;
    }

    @GetMapping("/export/{userId}")
    public void exportPnlCsv(@PathVariable Long userId,
                             HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=pnl_report.csv"
        );

        List<PnlResponse> pnlList =
            pnlService.calculateUnrealizedPnl(userId);

        PrintWriter writer = response.getWriter();
        writer.println("Asset,Quantity,AvgCost,CurrentPrice,InvestedValue,CurrentValue,UnrealizedPnL");

        for (PnlResponse p : pnlList) {
            writer.println(
                p.getAssetSymbol() + "," +
                p.getQuantity() + "," +
                p.getAvgCost() + "," +
                p.getCurrentPrice() + "," +
                p.getInvestedValue() + "," +
                p.getCurrentValue() + "," +
                p.getUnrealizedPnl()
            );
        }

        writer.flush();
        writer.close();
    }
}
