package com.example.printtest.controller;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public class PrintQueue {

    public static void main(String[] args) {
        String printerName = "Microsoft Print to PDF";
        int jobId = 1;

        // Get printer handle
        HANDLEByReference hPrinter = new HANDLEByReference();
        boolean success = Winspool.INSTANCE.OpenPrinter(printerName, hPrinter, null);
        if (!success) {
            throw new RuntimeException("Failed to open printer " + printerName);
        }

        // Get job info
        Pointer pJob = new Memory(1024);
        IntByReference pcbNeeded = new IntByReference();
        success = Winspool.INSTANCE.GetJob(hPrinter.getValue(), jobId, 2, pJob, 1024, pcbNeeded);
        if (!success) {
            throw new RuntimeException("Failed to get job information");
        }

        // Process job info
        JOB_INFO_2 jobInfo = new JOB_INFO_2(pJob);
        System.out.println("Job " + jobId + ": " + jobInfo.getStatusString());
    }

    public static interface Winspool extends StdCallLibrary {
        Winspool INSTANCE = (Winspool) Native.loadLibrary("Winspool.drv", Winspool.class, W32APIOptions.DEFAULT_OPTIONS);

        boolean OpenPrinter(String pPrinterName, HANDLEByReference phPrinter, Pointer pDefault);

        boolean GetJob(HANDLE hPrinter, int JobId, int Level, Pointer pJob, int cbBuf, IntByReference pcbNeeded);
    }

    public static class HANDLE extends PointerType {
        public HANDLE(Pointer address) {
            super(address);
        }

        public HANDLE() {
            super();
        }
    }

    public static class HANDLEByReference extends ByReference {
        public HANDLEByReference() {
            this(new HANDLE());
        }

        public HANDLEByReference(HANDLE h) {
            super(Pointer.SIZE);
            setValue(h);
        }

        public void setValue(HANDLE h) {
            getPointer().setPointer(0, h.getPointer());
        }

        public HANDLE getValue() {
            return new HANDLE(getPointer().getPointer(0));
        }
    }

    public static class JOB_INFO_2 {
        private int jobId;
        private String status;

        public JOB_INFO_2(Pointer pJob) {
            jobId = pJob.getInt(0);
            status = getStatus(pJob.getInt(156));
        }

        public int getJobId() {
            return jobId;
        }

        public String getStatusString() {
            return status;
        }

        private String getStatus(int statusCode) {
            switch (statusCode) {
                case 0x00000000:
                    return "Paused";
                case 0x00000001:
                    return "Error";
                case 0x00000002:
                    return "Deleting";
                case 0x00000003:
                    return "Spooling";
                case 0x00000004:
                    return "Printing";
                case 0x00000005:
                    return "Offline";
                case 0x00000006:
                    return "Paperout";
                case 0x00000007:
                    return "Printed";
                case 0x00000008:
                    return "Deleted";
                case 0x00000009:
                    return "Blocked DevQ";
                case 0x0000000a:
                    return "User Intervention Required";
                case 0x00000100:
                    return "Restarting";
                case 0x00000200:
                    return "Printing Wait";
                default:
                    return "Unknown";
            }
        }
    }
}
