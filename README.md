Install jenkins
`helm upgrade -i jenkins stable/jenkins -f k8s/jenkins/values.yaml`

To push images you need docker credentials.  
`k apply -f ~/.ssh/lohika/techtalk/creds_docker.yaml`

To access GitHub you need credentials.
Scanning works with https credentials

Git operations could work both with https and ssh credentials
`k create secret generic github-ssh-access --from-file=privateKey=${HOME}/.ssh/lohika/techtalk/deployKey --from-literal=username=git`
`k label secret github-ssh-access "jenkins.io/credentials-type"="basicSSHUserPrivateKey"`
`k annotate secret github-ssh-access "jenkins.io/credentials-description"="GitHub deployment key"` 

