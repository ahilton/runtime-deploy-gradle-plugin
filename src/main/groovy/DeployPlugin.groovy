import dsl.component.ArtifactDescriptor
import dsl.component.DeployableComponent
import dsl.plugin.DeployPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class DeployPlugin implements Plugin<Project> {
    void apply(Project project) {

        def deployPluginExtension = project.extensions.create('deploy', DeployPluginExtension)

        // Add the components container as an extension object
        def componentContainer = project.container(DeployableComponent)
        project.extensions.components = componentContainer

        // Create deployment task for each component
        componentContainer.withType(DeployableComponent.class, { dc ->
            project.task("deploy"+dc.getNameAsGradleCompatibleIdentifier(), type: DeployTask) {
                component = dc
            }
        })

        def dependencyContainer = project.container(ArtifactDescriptor)

        // Configure defaults
        componentContainer.all {
            group = deployPluginExtension.defaultGroup
            extension = deployPluginExtension.defaultExtension
            isDeployable = deployPluginExtension.isDeployable
            // Support dsl nesting. http://mrhaki.blogspot.com.au/2016/02/gradle-goodness-using-nested-domain.html?m=1
            dependencies = dependencyContainer
        }

        dependencyContainer.all {
            group = deployPluginExtension.defaultGroup
            extension = deployPluginExtension.defaultExtension
        }

        // Add a deployAll task which depends on all individual component deploy tasks
        project.task('deployAll').dependsOn(project.tasks.withType(DeployTask))
    }
}