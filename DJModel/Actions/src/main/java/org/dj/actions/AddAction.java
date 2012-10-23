/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.dj.cookies.api.AddCookie;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "DJModel",
id = "org.dj.actions.AddAction")
@ActionRegistration(iconBase = "org/dj/actions/add.png",
displayName = "#CTL_AddAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 100, separatorBefore = 50),
    @ActionReference(path = "Toolbars/File", position = 500)
})
@Messages("CTL_AddAction=Add")
public final class AddAction implements ActionListener {

    private final List<AddCookie> context;

    public AddAction(List<AddCookie> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for (AddCookie addCookie : context) {
            addCookie.run();
        }
    }
}
