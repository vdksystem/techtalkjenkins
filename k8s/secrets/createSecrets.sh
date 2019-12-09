k create secret generic github-ssh-access --from-file=privateKey=/Users/dkuleshov/.ssh/lohika/techtalk/deployKey --from-literal=username=git
k label secret github-ssh-access "jenkins.io/credentials-type"="basicSSHUserPrivateKey"
k annotate secret github-ssh-access "jenkins.io/credentials-description"="GitHub deployment key"