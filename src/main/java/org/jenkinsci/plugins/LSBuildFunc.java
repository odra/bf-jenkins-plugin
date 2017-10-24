package org.jenkinsci.plugins;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.inject.Inject;

public class LSBuildFunc extends AbstractStepImpl {
    private String path;

    @DataBoundConstructor
    public LSBuildFunc(){}

    @DataBoundSetter
    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    private static class LSBuildFuncExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {
        @Inject
        private transient LSBuildFunc step;

        @StepContextParameter
        @SuppressWarnings("unused")
        private transient hudson.model.Run build;

        @StepContextParameter
        @SuppressWarnings("unused")
        private transient FilePath workspace;

        @StepContextParameter
        @SuppressWarnings("unused")
        private transient Launcher launcher;

        @StepContextParameter
        @SuppressWarnings("unused")
        private transient TaskListener listener;

        @StepContextParameter
        private transient EnvVars env;

        @Override
        protected Void run() throws Exception {
            LSBuildStep ls = new LSBuildStep(step.path);

            ls.perform(build, workspace, launcher, listener);

            return null;
        }
    }

    @Extension(optional = true)
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {
        public DescriptorImpl() {
            super(LSBuildFunc.LSBuildFuncExecution.class);
        }

        @Override
        public String getFunctionName() {
            return "ls";
        }

        @Override
        public String getDisplayName() {
            return "Lists the contents of a folder";
        }
    }
}
