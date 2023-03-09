package com.example.printtest.controller;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class PrintB {
    public static void main(String[] args) throws PrinterException {
        // 构建打印请求属性集
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        // 设置打印格式，因为未确定类型，所以选择autosense
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        // 查找所有的可用的打印服务
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        // 定位默认的打印服务
        PrintService defaultService = PrintServiceLookup
                .lookupDefaultPrintService();

        // Create the print job
        DocPrintJob job = defaultService.createPrintJob();
        // Monitor print job events
        PrintJobWatcher pjDone = new PrintJobWatcher(job);
        // Wait for the print job to be done
        pjDone.waitForDone();
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintService(defaultService);
    }
}
