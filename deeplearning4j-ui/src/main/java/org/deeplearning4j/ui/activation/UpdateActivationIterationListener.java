package org.deeplearning4j.ui.activation;

import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.optimize.api.IterationListener;
import org.deeplearning4j.plot.iterationlistener.ActivationMeanIterationListener;
import org.deeplearning4j.plot.iterationlistener.PlotFiltersIterationListener;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Adam Gibson
 */
public class UpdateActivationIterationListener implements IterationListener {
    private Client client = ClientBuilder.newClient();
    private WebTarget target = client.target("http://localhost:8080").path("activations").path("update");
    private ActivationMeanIterationListener listener;
    private int iterations = 1;

    /**
     * Initializes with the variables to render filters for
     * @param iterations the number of iterations to update on
     */
    public UpdateActivationIterationListener(int iterations) {
        listener = new ActivationMeanIterationListener(iterations);
        this.iterations = iterations;
    }

    @Override
    public boolean invoked() {
        return false;
    }

    @Override
    public void invoke() {

    }

    @Override
    public void iterationDone(Model model, int iteration) {
        if(iteration % iterations == 0) {
            PathUpdate update = new PathUpdate();
            //update the weights
            listener.iterationDone(model, iteration);
            //ensure path is set
            update.setPath(listener.getOutputFile().getPath());
            //ensure the server is hooked up with the path
            target.request(MediaType.APPLICATION_JSON).post(Entity.entity(update, MediaType.APPLICATION_JSON));
        }

    }
}
