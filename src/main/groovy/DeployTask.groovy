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
        def c = component.get()

        if (!c.isDeployable){
            println ">>> Task $taskName [$c.name]. Component is not deployable. Skipping."
            return
        }

        // build dependency notation
        def extension = c.extension==null?'':"@$c.extension"
        def classifier = c.classifier==null?'':c.classifier
        def dependency = "$c.group:$c.id:$c.version:$classifier$extension"


        println " >>> Task $taskName [$c.name]. Resolving dependency: $dependency"

        // resolve dependency
        def configName = "configuration$c.name"
        def config = project.configurations.create(configName)
        project.dependencies.add(configName, dependency)
        def fs = config.resolve()
        println fs

        //TODO:: pass file and artifact id
    }
}