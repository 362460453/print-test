package com.example.printtest.controller;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public class JnaDemo {

    final static int WM_KEYDOWN = 0x0100;
    final static int WM_KEYUP = 0x0101;
    final static int WM_CHAR = 0x0102;
    final static int VK_A = 0x61;
    final static int SW_RESTORE = 0x09;

    public interface User32 extends StdCallLibrary, WinUser {

        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

        public abstract HWND FindWindow(String paramString1, String paramString2);
        public abstract HWND FindWindowEx(HWND hwndParent, HWND hwndChildAfter, String lpszClass, String lpszWindow);
        public abstract void PostMessage(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);
        public abstract void ShowWindow(HWND hWnd, int nCmdShow);
        public abstract LRESULT SendMessage(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);
        public abstract void SetForegroundWindow(HWND hWnd);
        public abstract void SetFocus(HWND hWnd);

    }

    public static void main(String[] args) {

        HWND parent = User32.INSTANCE.FindWindow("Notepad", null);
        System.out.println("parent " + (parent == null ? parent : parent.getPointer()));

        if (parent != null) {

            HWND child = User32.INSTANCE.FindWindowEx(parent, null, "Edit", null);
            System.out.println("child " + (child == null ? child : child.getPointer()));

            User32.INSTANCE.ShowWindow(parent, SW_RESTORE);
            User32.INSTANCE.SetForegroundWindow(parent);

            if (child != null) {
                User32.INSTANCE.SetFocus(child);

                WPARAM wPARAM = new WPARAM(VK_A);
                LPARAM lPARAM = new LPARAM(0);

                //User32.INSTANCE.PostMessage(child, WM_KEYDOWN, wPARAM, lPARAM);
                User32.INSTANCE.SendMessage(child, WM_KEYDOWN, wPARAM, lPARAM);
                User32.INSTANCE.SendMessage(child, WM_CHAR, wPARAM, lPARAM);
                User32.INSTANCE.SendMessage(child, WM_KEYUP, wPARAM, lPARAM);
            }
        }
    }
}
