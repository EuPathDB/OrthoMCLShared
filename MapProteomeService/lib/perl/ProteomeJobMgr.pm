package OrthoMCLShared::MapProteomeService::ProteomeJobMgr;

use strict;
use CBIL::Util::SshCluster;
use Carp;

sub new {
  my ($class, $configFile) = @_;

  my $self = {};
  bless($self,$class);
  $self->{configFile} = $configFile;
  return $self;
}

sub getConfig {
  my ($self, $tag) = @_;

  if (!$self->{config}) {
    open(F, $self->{configFile}) || $self->error("Can't open config file '$self->{configFile}'\n");

    while(<F>) {
      chomp;
      s/\s+$//;
      next if /^\#/ or /^$/;
      /^(\w+)\=(.+)/ || die "illegal line '$_' in config file '$self->{configFile}'\n";
      my $key=$1;
      my $val=$2;
      $self->{config}->{$key} = $val;
    }
  }
  $self->error("Config file does not have a property '$tag'") unless defined($self->{config}->{$tag});
  return $self->{config}->{$tag};
}

sub getCluster {
    my ($self) = @_;

    if (!$self->{cluster}) {
	my $clusterServer = $self->getConfig('clusterServer');
	my $clusterUser = $self->getConfig('clusterUserName');
	$self->{cluster} = CBIL::Util::SshCluster->new($clusterServer,
							  $clusterUser,
							  $self);
    }
    return $self->{cluster};
}

#include unused $testmode arg for compatibility w/ workflow code (SshCluster)
sub runCmd {
    my ($self, $testmode, $cmd) = @_;

    $self->log("Running cmd:\n$cmd\n");
    my $output = `$cmd`;
    my $status = $? >> 8;
    $self->error("Failed with status $status running: \n$cmd") if ($status);
    return $output;
}

sub runCmdInBackground {
    my ($self, $cmd) = @_;

    $self->log("Running cmd:\n$cmd &\n");
    system("$cmd &");
    my $status = $? >> 8;
    $self->error("Failed running '$cmd' with stderr:\n $!") if ($status);
}

sub error {
    my ($self, $msg) = @_;

    confess("$msg\n\n");
}

sub log {
  my ($self, $msg) = @_;
  print STDERR "$msg\n";
}

sub runClusterTask {
    my ($self, $user, $server, $processIdFile, $logFile, $controllerPropFile, $numNodes, $time, $queue, $ppn) = @_;

    # if not already started, start it up (otherwise the local process was restarted)
    if (!$self->clusterTaskRunning($processIdFile, $user, $server)) {
	my $cmd = "workflowRunDistribJob $controllerPropFile $logFile $processIdFile $numNodes $time $queue $ppn";
	my $sshCmd = "ssh -2 $user\@$server '/bin/bash -login -c \"$cmd\"'";
	$self->runCmdInBackground($sshCmd);
    }

    my $done = $self->runCmd(0, "ssh -2 $user\@$server '/bin/bash -login -c \"if [ -a $logFile ]; then tail -1 $logFile; fi\"'");

    return $done && $done =~ /Done/;
}

sub clusterTaskRunning {
    my ($self, $processIdFile, $user, $server) = @_;

    my $processId = `ssh -2 $user\@$server 'if [ -a $processIdFile ];then cat $processIdFile; fi'`;

    chomp $processId;

    my $status = 0;
    if ($processId) {
      system("ssh -2 $user\@$server 'ps -p $processId > /dev/null'");
      $status = $? >> 8;
      $status = !$status;
    }
    return $status;
}

1;
