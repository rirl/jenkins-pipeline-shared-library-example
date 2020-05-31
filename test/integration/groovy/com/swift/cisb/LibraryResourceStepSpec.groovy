package com.swift.cisb

import hudson.model.queue.QueueTaskFuture
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Specification

class LibraryResourceStepSpec extends Specification {
  @Rule
  public JenkinsRule rule = new JenkinsRule()

  void setup() {
    RuleBootstrapper.setup(rule)
  }

  def "libraryResource can load resources in library"() {
    given:
    final CpsFlowDefinition flow = new CpsFlowDefinition('''
      final resource = libraryResource('com/swift/cisb/lorumipsum.txt')
      echo "Resource Text: $resource"
    '''.stripIndent(), true)
    final WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'project')
    workflowJob.definition = flow

    when:
    final QueueTaskFuture<WorkflowRun> futureRun = workflowJob.scheduleBuild2(0)

    then:
    final WorkflowRun run = rule.assertBuildStatusSuccess(futureRun)
    rule.assertLogContains('Resource Text: Lorem ipsum dolor sit amet', run)
  }
}
