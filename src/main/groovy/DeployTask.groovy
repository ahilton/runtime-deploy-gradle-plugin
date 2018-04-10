import dsl.component.ArtifactDescriptor
import dsl.component.DeployableComponent
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class DeployTask extends DefaultTask {

    // https://docs.gradle.org/4.6/userguide/lazy_configuration.html
    @Input
    final Property<DeployableComponent> component = project.objects.property(DeployableComponent)

    @TaskAction
    void performComponentDeploy() {
        def taskName = getName()
        println "  --- $taskName ---"

        def c = component.get()

        if (!c.isDeployable){
            println " --- $taskName --- [$c.name]. Component is not deployable. Skipping."
            return
        }

        // deploy dependent components
        c.dependencies.each {
            doDeploy(it)
        }

        // deploy main component
        doDeploy(c)
    }

    boolean doDeploy(ArtifactDescriptor descriptor){
        def dependencyNotation = descriptor.buildDependencyNotation()
        println " >>> Resolving "+dependencyNotation

        // Create a configuration against which the dependency will be resolved
        def id = descriptor.getNameAsGradleCompatibleIdentifier()
        def configName = "configuration$id"
        def config = project.configurations.create(configName)
        project.dependencies.add(configName, dependencyNotation)

        def fs = config.resolve()
        println fs
    }
}