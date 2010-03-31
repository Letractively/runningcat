/*
 */
package ProcessExecuting;

import ProcessExecuting.Exceptions.ProcessRunningException;
import ProcessExecuting.Exceptions.ProcessNotRunningException;
import ProcessExecuting.Exceptions.ProcessCanNotBeRunException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author partizanka
 */
public class ProcessExecutor {

    private Process process = null;
    private Timer timer = null;
    private String[] cmd = null;
    private File curDir = null;
    private long timeLimit = 0, beginTime, endTime;
    private InputStream errorStream, inputStream;
    private OutputStream outputStream;

    public ProcessExecutor(String[] cmd, String curDir, long timeLimit) {
        this.cmd = cmd;
        if (curDir != null && curDir.compareTo("") != 0) {
            this.curDir = new File(curDir);
        } else {
            this.curDir = null;
        }
        this.timeLimit = timeLimit;
    }

    public void execute() throws ProcessRunningException, ProcessCanNotBeRunException {
        if (process == null) {
            if (cmd != null) {
                try {
                    process = Runtime.getRuntime().exec(cmd, null, curDir); // throws IOException
                    inputStream = process.getInputStream();
                    outputStream = process.getOutputStream();
                    errorStream = process.getErrorStream();
                } catch (IOException ex) {
                    throw new ProcessCanNotBeRunException("An I/O error occurs. Process cannot be run: " + ex);
                }
                timer = new Timer();
                timer.schedule(new KillingTimerTask(process), timeLimit + 100); // it will kill the program if time limit exceeded
                beginTime = System.currentTimeMillis();
            } else {
                throw new ProcessCanNotBeRunException("Command to run is null");
            }
        } else {
            throw new ProcessRunningException("Process is already running");
        }
    }

    public boolean isRunning() {
        return process != null;
    }

    public int waitForExit() throws ProcessNotRunningException, InterruptedException {
        if (process != null) {
            try {
                int exitCode = process.waitFor(); // throws InterruptedException if the process was killed by timer
                endTime = System.currentTimeMillis();
                return exitCode;
            } finally {
                timer.cancel();
            }
        } else {
            throw new ProcessNotRunningException("Process is not running");
        }
    }

    public boolean isOutOfTime() {
        return (endTime - beginTime >= timeLimit);
    }

    public long getWorkTime() {
        return endTime - beginTime;
    }

    public InputStream getInputStream() throws ProcessNotRunningException {
        if (process != null) {
            return inputStream;// process.getInputStream();
        } else {
            throw new ProcessNotRunningException("Process is not running");
        }
    }

    public OutputStream getOutputStream() throws ProcessNotRunningException {
        if (process != null) {
            return outputStream;// process.getOutputStream();
        } else {
            throw new ProcessNotRunningException("Process is not running");
        }
    }

    public InputStream getErrorStream() throws ProcessNotRunningException {
        if (process != null) {
            return errorStream;// process.getErrorStream();
        } else {
            throw new ProcessNotRunningException("Process is not running");
        }
    }

    private class KillingTimerTask extends TimerTask {

        private Process process;

        public KillingTimerTask(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            process.destroy();
        }
    }

    public static void main(String[] argv) {
        ProcessExecutor executor = new ProcessExecutor(new String[]{"test53769.exe"}, null, 1000);
        try {
            executor.execute();
        } catch (ProcessRunningException ex) {
            ex.printStackTrace();
        } catch (ProcessCanNotBeRunException ex) {
            ex.printStackTrace();
        } finally {
            try {
                int code = executor.waitForExit();
                if (executor.isOutOfTime()) {
                    System.err.println("Program is out of time");
                }
                if (code != 0) {
                    System.err.println("Program failed with run time error");
                }
            } catch (ProcessNotRunningException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
