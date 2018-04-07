import org.gradle.api.Plugin
import org.gradle.api.Project

class DeployPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Add the 'greeting' extension object
        def extension = project.extensions.create('greeting', DeployPluginExtension)
        // Add a task that uses configuration from the extension object
        project.task('hello', type: Deploy) {
            doLast {
                println extension.message
            }
        }
    }
}