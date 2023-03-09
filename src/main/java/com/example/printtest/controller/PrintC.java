//package com.example.printtest.controller;
//
//public class PrintC {
//    /// <summary>
//    /// 获取打印机的打印列表
//    /// </summary>
//    /// <param name="printName">打印机名称，本地</param>
//    /// <returns>返回打印队列中文档名称字符串，多个之间用逗号连接</returns>
//    public static String GetPrintJobs(String printName)
//    {
//        StringBuilder result = new StringBuilder();
//
//        IntPtr handle;
//        int FirstJob = 0;
//        int NumJobs = 127;
//        int pcbNeeded;
//        int pcReturned;
//
//        // open printer
//        OpenPrinter(printName, out handle, IntPtr.Zero);
//
//        // get num bytes required, here we assume the maxt job for the printer quest is 128 (0..127)
//        EnumJobs(handle, FirstJob, NumJobs, 1, IntPtr.Zero, 0, out pcbNeeded, out pcReturned);
//
//        // allocate unmanaged memory
//        IntPtr pData = Marshal.AllocHGlobal(pcbNeeded);
//
//        // get structs
//        EnumJobs(handle, FirstJob, NumJobs, 1, pData, pcbNeeded, out pcbNeeded, out pcReturned);
//
//        // create array of managed job structs
//        JOB_INFO_1[] jobs = new JOB_INFO_1[pcReturned];
//
//        // marshal struct to managed
//        int pTemp = pData.ToInt32(); //start pointer
//        for (int i = 0; i < pcReturned; ++i)
//        {
//            jobs[i] = (JOB_INFO_1)Marshal.PtrToStructure(new IntPtr(pTemp), typeof(JOB_INFO_1));
//            result.Append(jobs[i].pDocument);
//            result.Append(",");
//            pTemp += Marshal.SizeOf(typeof(JOB_INFO_1));
//        }
//
//        // cleanup unmanaged memory
//        Marshal.FreeHGlobal(pData);
//
//        // close printer
//        ClosePrinter(handle);
//
//        return result.ToString();
//    }
//}
