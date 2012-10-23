/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.dj.editor.api.DJEditCookie;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "DJModel",
id = "org.dj.actions.EditAction")
@ActionRegistration(iconBase = "org/dj/actions/edit.png",
displayName = "#CTL_EditAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 156),
    @ActionReference(path = "Toolbars/File", position = 650)
})
@Messages("CTL_EditAction=Edit")
public final class EditAction implements ActionListener {

    private final List<DJEditCookie> context;

    public EditAction(List<DJEditCookie> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for (DJEditCookie dJEditCookie : context) {
            dJEditCookie.run();
        }
    }
}
