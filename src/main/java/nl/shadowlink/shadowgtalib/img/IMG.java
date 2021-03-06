/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.shadowlink.shadowgtalib.img;

import nl.shadowlink.file_io.ReadFunctions;
import nl.shadowlink.file_io.WriteFunctions;
import nl.shadowlink.shadowgtalib.model.model.Model;
import nl.shadowlink.shadowgtalib.texturedic.TextureDic;
import nl.shadowlink.shadowgtalib.utils.Constants;
import nl.shadowlink.shadowgtalib.utils.Constants.GameType;
import nl.shadowlink.shadowgtalib.utils.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Kilian
 */
public class IMG {
    private String fileName;
    private GameType mGameType;
    public boolean changed = false; // True when the file needs to be saved

    public boolean encrypted = false;
    public boolean containsProps = false;

    public byte[] key = new byte[32];

    public int itemCount = 0;
    public int cutCount = 0;
    public int wtdCount = 0;
    public int wbdCount = 0;
    public int wbnCount = 0;
    public int wplCount = 0;
    public int wddCount = 0;
    public int wdrCount = 0;
    public int wadCount = 0;
    public int wftCount = 0;
    public int unknownCount = 0;

    public ArrayList<IMG_Item> Items = new ArrayList(); // All items

    public IMG(String fileName, final GameType pGameType, byte[] key, boolean autoLoad, boolean containsProps) {
        this.key = key;
        // Message.displayMsgSuper("Loading IMG: " + fileName);
        this.fileName = fileName;
        mGameType = pGameType;
        this.containsProps = containsProps;
        if (autoLoad)
            loadImg();
    }

    private boolean loadImg() {
        switch (mGameType) {
            case GTA_III:
                new IMG_III().loadImg(this);
                break;
            case GTA_VC:
                new IMG_VC().loadImg(this);
                break;
            case GTA_SA:
                new IMG_SA().loadImg(this);
                break;
            case GTA_IV:
                new IMG_IV().loadImg(this);
                break;
        }
        if (Items == null)
            return false;
        else
            return true;
    }

    public int getItemIndex(String name) {
        int i = 0;
        while (!Items.get(i).getName().equalsIgnoreCase(name)) {
            if (i < Items.size() - 1) {
                i++;
            } else {
                break;
            }
        }
        return i;
    }

    public IMG_Item findItem(String name) {
        IMG_Item ret = null;
        int i = 0;
        while (!Items.get(i).getName().equalsIgnoreCase(name)) {
            if (i < Items.size() - 1) {
                i++;
            } else {
                break;
            }
        }
        if (Items.get(i).getName().equalsIgnoreCase(name)) {
            // Message.displayMsgSuper("<IMG " + fileName + ">Found file " + name + " at " + i + " offset " +
            // Items.get(i).getOffset());
            ret = Items.get(i);
        } else {
            // Message.displayMsgSuper("<IMG " + fileName + ">Unable to find file " + name);
        }
        return ret;
    }

    public void addItem(Model mdl, String name) {
        WriteFunctions wf = new WriteFunctions();
        if (wf.openFile(fileName)) {
            name = name.toLowerCase();
            name = name.replaceAll(".dff", ".wdr");
            IMG_Item tempItem = new IMG_Item();
            tempItem.setName(name);
            tempItem.setType(Constants.rtWDR);
            tempItem.setOffset(wf.getFileSize());
            wf.gotoEnd();
            mdl.convertToWDR(wf);
            tempItem.setSize(mdl.size);
            tempItem.setFlags(mdl.flags);

            Items.add(tempItem);
            if (wf.closeFile()) {
                System.out.println("Closed file");
            } else {
                System.out.println("Unable to close the file");
            }
            wf = null;
            changed = true;
        } else {
            // JOptionPane.show//MessageDialog(this, "Unable to open " + fileName
            // + " for writing!");
        }
    }

    public void addItem(TextureDic txd, String name) {
        WriteFunctions wf = new WriteFunctions();
        if (wf.openFile(fileName)) {
            name = name.toLowerCase();
            name = name.replaceAll(".txd", ".wtd");
            IMG_Item tempItem = new IMG_Item();
            tempItem.setName(name);
            tempItem.setType(Constants.rtWTD);
            tempItem.setOffset(wf.getFileSize());
            wf.gotoEnd();
            txd.convertToWTD(wf);
            tempItem.setSize(txd.size);
            tempItem.setFlags(txd.flags);

            Items.add(tempItem);
            if (wf.closeFile()) {
                System.out.println("Closed file");
            } else {
                System.out.println("Unable to close the file");
            }
            wf = null;
            changed = true;
        } else {
            // JOptionPane.show//MessageDialog(this, "Unable to open " + fileName
            // + " for writing!");
        }
    }

    public void addItem(File file) {
        if (file.isFile() && file.canRead()) {
            ReadFunctions rf = new ReadFunctions();
            System.out.println("File: " + file.getAbsolutePath());
            if (rf.openFile(file.getAbsolutePath())) {
                WriteFunctions wf = new WriteFunctions();
                if (wf.openFile(fileName)) {
                    System.out.println("File size: " + file.length());
                    byte[] newFile = rf.readArray((int) file.length());
                    IMG_Item tempItem = new IMG_Item();
                    tempItem.setName(file.getName());
                    tempItem.setType(Utils.getResourceType(file.getName()));
                    tempItem.setOffset(wf.getFileSize());
                    tempItem.setSize((int) file.length());
                    if (tempItem.isResource()) {
                        rf.seek(0x8);
                        tempItem.setFlags(rf.readInt());
                    }
                    Items.add(tempItem);
                    wf.gotoEnd();
                    wf.writeArray(newFile);
                    if (wf.closeFile()) {
                        System.out.println("Closed file");
                    } else {
                        System.out.println("Unable to close the file");
                    }
                    newFile = null;
                    wf = null;
                    changed = true;
                } else {
                    // JOptionPane.show//MessageDialog(this, "Unable to open " +
                    // fileName + " for writing!");
                }
                rf.closeFile();
                rf = null;
            } else {
                // JOptionPane.show//MessageDialog(this, "Unable to open " +
                // file.getName() + " for reading!");
            }
        }
    }

    public ArrayList<IMG_Item> getItems() {
        return Items;
    }

    public void setItems(ArrayList<IMG_Item> Items) {
        this.Items = Items;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public GameType getGameType() {
        return mGameType;
    }

    public void setGameType(final GameType gameType) {
        mGameType = gameType;
    }

    public void save() {
        switch (mGameType) {
        /* case Finals.gIII: new IMG_III().saveImg(this); break; case Finals.gVC: new IMG_VC().saveImg(this); break;
		 * case Finals.gSA: new IMG_SA().saveImg(this); break; */
            case GTA_IV:
                new IMG_IV().saveImg(this);
                break;
        }
    }

}
