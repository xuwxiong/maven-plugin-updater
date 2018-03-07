#!Groovy
properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '10'))])
node('mtl2020 && linux'){
    stage ('Checkout'){
        checkout scm
    }
    // Mark the code build
    stage ('Build'){
        withMaven(jdk: 'java6_64', maven: 'maven-3.0.5') {
            sh "mvn -Penforce-snapshot,MVN_TOOLCHAINS clean deploy"
        }
    }
}