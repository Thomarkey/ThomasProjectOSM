package country.customer.project.selenium.driver.setup;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.*;
import com.github.dockerjava.core.command.PullImageResultCallback;
import country.customer.project.selenium.driver.DriverProvider;
import country.customer.project.selenium.driver.setup.enums.EnvironmentType;
import country.customer.project.selenium.support.ResponseHandler;
import java.util.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.awaitility.Awaitility;
import org.awaitility.Duration;

public class DockerProvider {

    private final String stableTag = "3.141.59-xenon";
    private CreateContainerResponse standaloneContainer;
    private List<CreateContainerResponse> chromeNodeContainer = new ArrayList<>();
    private List<CreateContainerResponse> firefoxNodeContainer = new ArrayList<>();
    private DockerClient dockerClient;
    private String gridId;
    private static DockerProvider instance;

    public static DockerProvider getInstance() {
        if (instance == null) {
            instance = new DockerProvider();
        }
        return instance;
    }

    /**
     * Setup of dockerClient. dockerClient is needed in every function of this class.
     */
    private DockerProvider() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        dockerClient = DockerClientBuilder.getInstance(config).build();
    }

    public void setupStandAlone() {
        setupStandAlone(stableTag);
    }

    public void setupStandAlone(String tag) {
        if (isDocker()) {
            switch (DriverProvider.getBrowser()) {
                case CHROME:
                    setupStandAloneChrome(tag);
                    break;
                case FIREFOX:
                    setupStandAloneFirefox(tag);
                    break;
                default:
                    throw new RuntimeException("This option is not yet supported by docker! " +
                            "If you think it is, contact the maintainer of this library");
            }
        }
    }

    /**
     * Sets up the current "stable" version of chrome according to the maintainer of the library
     */
    public void setupStandAloneChrome() {
        if (isDocker()) {
            setupStandAlone("selenium/standalone-chrome", stableTag);
        }
    }

    /**
     * Sets up the current "stable" version of firefox according to the maintainer of the library
     */
    public void setupStandAloneFirefox() {
        if (isDocker()) {
            setupStandAlone("selenium/standalone-firefox", stableTag);
        }
    }

    /**
     * @param tag -> Tag that should be downloaded for standalone chrome
     */
    public void setupStandAloneChrome(String tag) {
        if (isDocker()) {
            setupStandAlone("selenium/standalone-chrome", tag);
        }
    }

    /**
     * @param tag -> Tag that should be downloaded for standalone firefox
     */
    public void setupStandAloneFirefox(String tag) {
        if (isDocker()) {
            setupStandAlone("selenium/standalone-firefox", tag);
        }
    }

    /**
     * Teardown of the standalone container
     */
    public void teardownStandAlone() {
        if (isDocker()) {
            dockerClient.killContainerCmd(standaloneContainer.getId()).exec();
            dockerClient.removeContainerCmd(standaloneContainer.getId()).exec();
        }
    }

    /**
     * @param scale -> amount of nodes of firefox + chrome
     *              Setups a docker grid!
     *              tag is the current "stable" version of hub according to the maintainer of the library
     */
    public void setupDockerGrid(int scale) {
        setupDockerGrid(scale, stableTag);
    }

    public void setupDockerGrid(String scale) {
        setupDockerGrid(Integer.parseInt(scale));
    }

    /**
     * @param scale -> Amount of nodes of firefox + chrome
     * @param tag   -> Tag of hub/nodes that should be pulled and started
     */
    public void setupDockerGrid(int scale, String tag) {
        if (isDocker()) {
            clearSeleniumStandAloneDockers();
            pruneNetworks();
            setupSeleniumHub(tag);
            addChromeNodes(1, tag);
            addFirefoxNodes(1, tag);
            addChromeNodes(scale - 1, tag);
            addFirefoxNodes(scale - 1, tag);
        }
    }

    public void setupDockerGrid(String scale, String tag) {
        setupDockerGrid(Integer.parseInt(scale), tag);
    }

    /**
     * @param scale -> Amount of chrome nodes to add
     *              tag is the current "stable" version of chrome according to the maintainer of the library
     */
    public void addChromeNodes(int scale) {
        if (isDocker()) {
            //Creating the nodes image. Scale = amount of firefox/chrome nodes
            addSeleniumNode(scale, chromeNodeContainer, "selenium/node-chrome", stableTag);
        }
    }

    /**
     * @param scale -> Amount of firefox nodes to add
     *              tag is the current "stable" version of firefox according to the maintainer of the library
     */
    public void addFirefoxNodes(int scale) {
        if (isDocker()) {
            //Creating the nodes image. Scale = amount of firefox/chrome nodes
            addSeleniumNode(scale, firefoxNodeContainer, "selenium/node-firefox", stableTag);
        }
    }

    /**
     * @param scale -> Amount of chrome nodes to add
     * @param tag   -> Tag of "selenium/node-chrome" that should be pulled and started
     */
    public void addChromeNodes(int scale, String tag) {
        if (isDocker()) {
            //Creating the nodes image. Scale = amount of firefox/chrome nodes
            addSeleniumNode(scale, chromeNodeContainer, "selenium/node-chrome", tag);
        }
    }

    /**
     * @param scale -> Amount of firefox nodes to add
     * @param tag   -> Tag of "selenium/node-firefox" that should be pulled and started
     */
    public void addFirefoxNodes(int scale, String tag) {
        if (isDocker()) {
            //Creating the nodes image. Scale = amount of firefox/chrome nodes
            addSeleniumNode(scale, firefoxNodeContainer, "selenium/node-firefox", tag);
        }
    }

    /**
     * Teardown of the docker grid
     */
    public void tearDownDockerGrid() {
        if (isDocker()) {
            //Kill and remove all chrome nodes
            chromeNodeContainer.forEach(container -> dockerClient.killContainerCmd(container.getId()).exec());
            chromeNodeContainer.forEach(container -> dockerClient.removeContainerCmd(container.getId()).exec());

            //Kill and remove all firefox nodes
            firefoxNodeContainer.forEach(container -> dockerClient.killContainerCmd(container.getId()).exec());
            firefoxNodeContainer.forEach(container -> dockerClient.removeContainerCmd(container.getId()).exec());
        }
    }

    /*********************************
     * HELPERS
     *********************************
     *
     * @param containerName -> name of the container e.g. selenium/standalone-chrome.
     * @param tag           -> tag you'd like to run.
     *                      Only 1 standalone container is allowed to run at a time.
     */
    private void setupStandAlone(String containerName, String tag) {
        if (standaloneContainer != null) {
            throw new RuntimeException("There is already a standalone running! Review your code for duplicate start-ups.");
        }

        clearSeleniumDockers();
        pruneNetworks();

        PortSetup portSetup = new PortSetup();

        pullDockerImage(containerName, tag);

        Volume downloadFolderVolume = new Volume("/home/seluser/Downloads");

        standaloneContainer = dockerClient
                .createContainerCmd(containerName + ":" + tag)
                .withExposedPorts(portSetup.getExposedPorts())
                .withVolumes(downloadFolderVolume)
                .withHostConfig(HostConfig.newHostConfig()
                        .withMemory(1024000000L)
                        .withShmSize(2000000000L)
                        .withBinds(new Bind("/tmp", downloadFolderVolume))
                        .withPortBindings(portSetup.getPortBindings()))
                .exec();

        dockerClient.startContainerCmd(standaloneContainer.getId()).exec();
        waitForDockerToBeReady();
    }

    /**
     * Setups the network which the selenium grid uses to communicate
     */
    private void setupGridNetwork() {
        //Creating the network (Node + Hub use this to communicate with each other)
        gridId = dockerClient.createNetworkCmd()
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withDriver("bridge")
                .exec().getId();
    }

    /**
     * Setups the selenium hub.
     */
    private void setupSeleniumHub(String tag) {
        List<Container> dockerSearch = dockerClient
                .listContainersCmd()
                .withShowAll(true)
                .withNameFilter(Collections.singleton("selenium-hub"))
                .exec();

        if (dockerSearch.size() > 0) {
            Container hubContainer = dockerSearch.get(0);

            if (!StringUtils.containsIgnoreCase(hubContainer.getStatus(), "up")) {
                dockerClient.startContainerCmd(hubContainer.getId()).exec();
            }

            gridId = hubContainer.getHostConfig().getNetworkMode();
        } else {
            clearSeleniumDockers();
            setupGridNetwork();

            PortSetup portSetup = new PortSetup();

            pullDockerImage("selenium/hub", tag);

            //Creating the hub
            CreateContainerResponse hubContainer = dockerClient
                    .createContainerCmd("selenium/hub:" + tag)
                    .withExposedPorts(portSetup.getExposedPorts())
                    .withHostConfig(HostConfig.newHostConfig()
                            .withPortBindings(portSetup.getPortBindings())
                            .withNetworkMode(gridId))
                    .withName("selenium-hub")
                    .exec();

            //Starting the hub
            dockerClient.startContainerCmd(hubContainer.getId()).exec();
        }
    }

    /**
     * Add a selenium node to the grid.
     */
    private void addSeleniumNode(int scale, List<CreateContainerResponse> nodeContainerList, String containerName, String tag) {
        pullDockerImage(containerName, tag);

        List<CreateContainerResponse> containersToStart = new ArrayList<>();
        Volume downloadFolderVolume = new Volume("/home/seluser/Downloads");

        for (int i = 0; i < scale; i++) {
            containersToStart.add(
                    dockerClient
                            .createContainerCmd(containerName + ":" + tag)
                            .withEnv(getNodeEnvironments())
                            .withVolumes(downloadFolderVolume)
                            .withHostConfig(HostConfig.newHostConfig()
                                    .withMemory(1024000000L)
                                    .withShmSize(2000000000L)
                                    .withBinds(new Bind("/tmp", downloadFolderVolume))
                                    .withNetworkMode(gridId))
                            .exec()
            );
        }

        //Starting the defined firefox nodes
        containersToStart.forEach(container ->
                dockerClient.startContainerCmd(container.getId()).exec());
        nodeContainerList.addAll(containersToStart);
        waitForDockerToBeReady();
    }

    /**
     * Pulls a docker image with output.
     */
    private void pullDockerImage(String containerName, String tag) {
        try {
            dockerClient.pullImageCmd(containerName)
                    .withTag(tag)
                    .exec(new PullImageResultCallback() {
                        public void onNext(PullResponseItem item) {
                            super.onNext(item);
                            if (item.getId() == null) {
                                System.out.println(item.getStatus());
                            } else {
                                System.out.println(item.getId() + " : " + item.getStatus());
                            }
                        }
                    }).awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException("Got interrupted while download a docker image! \n" + e);
        }
    }

    /**
     * Waits for docker to be ready.
     */
    private void waitForDockerToBeReady() {
        Awaitility.await()
                .pollInterval(org.awaitility.Duration.FIVE_SECONDS)
                .timeout(Duration.ONE_MINUTE)
                .ignoreExceptions()
                .until(() -> ResponseHandler.getInstance().isDockerReady());
    }

    /**
     * Some default environments the nodes need.
     */
    private List<String> getNodeEnvironments() {
        List<String> nodeEnvs = new ArrayList<>();
        nodeEnvs.add("SCREEN_WIDTH=1920");
        nodeEnvs.add("SCREEN_HEIGHT=1080");
        nodeEnvs.add("SCREEN_DEPTH=24");
//        nodeEnvs.add("HUB_HOST=selenium-hub");
        //TODO: deeshierboveisjuiste,eronderistest
        nodeEnvs.add("HUB_HOST=selenium-ch");
        nodeEnvs.add("HUB_PORT=4444");
        return nodeEnvs;
    }

    private void clearSeleniumStandAloneDockers() {
        List<Container> dockerSearch = dockerClient.listContainersCmd().withShowAll(true).exec();

        dockerSearch.stream()
                .filter(d -> d.getImage().contains("selenium/standalone"))
                .forEach(d -> {
                    System.out.println("Force removing a docker container with name: " + d.getImage());
                    dockerClient.removeContainerCmd(d.getId()).withForce(true).exec();
                });
    }

    private void clearSeleniumDockers() {
        List<Container> dockerSearch = dockerClient.listContainersCmd().withShowAll(true).exec();

        dockerSearch.stream()
                .filter(d -> d.getImage().contains("selenium/"))
                .forEach(d -> {
                    System.out.println("Force removing a docker container with name: " + d.getImage());
                    dockerClient.removeContainerCmd(d.getId()).withForce(true).exec();
                });
    }

    private void pruneNetworks() {
        dockerClient.pruneCmd(PruneType.NETWORKS).exec();
    }

    private boolean isDocker() {
        return (EnvironmentType.fromString(System.getProperty("executionMode", "local")).equals(EnvironmentType.DOCKER));
    }

    /**
     * InnerClass for Port setup of the standalone + hub containers.
     */
    private class PortSetup {

        private List<ExposedPort> exposedPorts;
        private Ports portBindings;

        public PortSetup() {
            exposedPorts = new ArrayList<>();
            exposedPorts.add(ExposedPort.tcp(4444));
            exposedPorts.add(ExposedPort.tcp(5899));

            setPortBindings();
        }

        private void setPortBindings() {
            portBindings = new Ports();
            exposedPorts.forEach(
                    port -> portBindings.bind(port, Ports.Binding.bindPort(port.getPort()))
            );
        }

        public List<ExposedPort> getExposedPorts() {
            return exposedPorts;
        }

        public void addExposedPort(int port) {
            exposedPorts.add(ExposedPort.tcp(port));
            setPortBindings();
        }

        public Ports getPortBindings() {
            return portBindings;
        }
    }
}
