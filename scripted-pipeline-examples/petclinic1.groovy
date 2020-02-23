node {
    notify('Started')
    try {
        stage 'checkout'
        git changelog: false, poll: false, url: 'https://github.com/levops-cloud/jenkins2-course-spring-petclinic.git'
    
        stage 'compiling, test, packaging'
        
            sh 'mvn clean package'
        
            
    
    notify('Success')
    } catch (err) {
        notify("Error ${err}")
        currentBuild.result = 'FAILURE'
    }
            stage 'archiving artifacts'
            step([$class: 'JUnitResultArchiver', testResults: 'target/surefire-reports/*xml'])
            archiveArtifacts "target/*war"
}

def notify(status){
    emailext (
      to: "lev@gmail.com",
      subject: "${status}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
      body: """<p>${status}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
        <p>Check console output at <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>""",
    )
}
