import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

class DeployPlugin implements Plugin<Project> {
    void apply(Project project) {

        // Add the 'greeting' extension object
        def extension = project.extensions.create('deploy', DeployPluginExtension)

        // Create a container of Book instances
        def components = project.container(DeployableComponent)
        components.withType(DeployableComponent.class, new Action<DeployableComponent>() {
            @Override
            void execute(DeployableComponent deployableComponent) {
                project.task("deploy"+deployableComponent.name.capitalize(), type: DeployTask) {
                    component = deployableComponent
                }
            }
        })
        components.all {
            group = extension.defaultGroup
        }
        // Add the container as an extension object
        project.extensions.components = components

        // Add a task that uses configuration from the extension object
        project.task('deploy') {
            doLast {
                components.each { component ->
                    println "$component.name -> $component.group"
                }
            }
        }
    }
}