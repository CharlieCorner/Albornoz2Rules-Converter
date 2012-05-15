package albornoz2rulesconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author charlie
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            List<String> lista = rulesFileCreator(fc.getSelectedFile());
            escribirArchivoRules(lista, fc.getSelectedFile().getCanonicalPath());
        }
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public static List<String> rulesFileCreator(File albornoz) {
        List<String> listaReglas = new ArrayList<>();
        FileReader fr = null;
        BufferedReader br;
        try {
            fr = new FileReader(albornoz);
            br = new BufferedReader(fr);

            String linea;
            StringBuilder regla = new StringBuilder();

            while ((linea = br.readLine()) != null) {
                //System.out.println(linea);
                if (linea.startsWith("UD") || 0 == linea.length()) {
                    continue;
                } else {

                    if (linea.startsWith("IF")) {

                        if (regla.length() != 0) {
                            listaReglas.add(regla.toString());
                        }
                        String lineaAParsear[] = linea.split("\\s");
                        regla = new StringBuilder();
                        regla = regla.append(lineaAParsear[1].replaceAll("\\s", ""));

                    } else if (linea.startsWith("AND")) {
                        String lineaAParsear[] = linea.split("\\s");
                        regla = regla.append('^').append(lineaAParsear[1].replaceAll("\\s", ""));

                    } else if (linea.startsWith("THEN")) {
                        String lineaAParsear[] = linea.split("\\s");
                        regla = regla.append('=');
                        regla = regla.append(lineaAParsear[1].replaceAll("\\s", ""));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return listaReglas;
    }

    private static void imprimirLista(List<String> lista) {
        for (String s : lista) {
            System.out.println(s);
        }
    }

    private static void escribirArchivoRules(List<String> lista, String dir) throws IOException {
        File f = new File(dir.replace("base2.albornoz", "reglasAlbornoz.rules"));
        if (!f.createNewFile()) {
            System.err.println("Hubo un problema al crear el archivo " + f.exists());
        } else {
            PrintWriter out = new PrintWriter(new FileWriter(f));
            for (String linea : lista) {
                out.println(linea);
            }
            out.close();
        }
    }
}
