package Server.Command;

import Model.Resource;
import Service.ResourceService;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditFileAction extends ControllerBaseAction {

    public EditFileAction(Socket socket) {
        super(socket);
    }

    @Override
    public boolean execute() {
        AtomicBoolean result = new AtomicBoolean(false);
        try {
            respondClientOk();
            String fileIdToEdit = this.bufferedInputReader.readLine();

            if (fileIdToEdit != null) {
                respondClientOk();
                ResourceService resourceService = new ResourceService();
                Resource resourceToUpdate = resourceService.findById(Integer.parseInt(fileIdToEdit));
                String fileToEdit = this.bufferedInputReader.readLine();

                if (resourceToUpdate != null) {
                    editFile(resourceToUpdate, fileToEdit);
                    this.bufferedOutputWriter.write("Success");

                    result.set(true);
                }
                else {
                    this.bufferedOutputWriter.write("Failed");
                }
                this.bufferedOutputWriter.flush();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return result.get();
    }

    private void editFile(Resource resourceToUpdate, String fileToEdit) {
        ResourceService resourceService = new ResourceService();

        byte[] result = fileToEdit.getBytes(StandardCharsets.ISO_8859_1);

        resourceToUpdate.setFile(result);
        resourceService.updateFile(resourceToUpdate);
    }
}
