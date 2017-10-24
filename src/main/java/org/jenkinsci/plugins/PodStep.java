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

public class PodStep extends AbstractStepImpl {
    private String repo;

    @DataBoundConstructor
    public PodStep() {}

    @DataBoundSetter
    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getRepo() {
        return this.repo;
    }

    private static class PodStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {
        @Inject
        private transient PodStep step;

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
            PodBuild pod = new PodBuild(step.repo);

            pod.perform(build, workspace, launcher, listener);

            return null;
        }
    }

    @Extension(optional = true)
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {
        public DescriptorImpl() {
            super(PodStep.PodStepExecution.class);
        }

        @Override
        public String getFunctionName() {
            return "pod";
        }

        @Override
        public String getDisplayName() {
            return "Updates local cocoapods repository";
        }
    }
}
