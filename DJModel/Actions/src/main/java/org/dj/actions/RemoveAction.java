/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import org.dj.cookies.api.RemoveCookie;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "DJModel",
id = "org.dj.actions.RemoveAction")
@ActionRegistration(iconBase = "org/dj/actions/Remove.png",
displayName = "#CTL_RemoveAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 150, separatorAfter = 175),
    @ActionReference(path = "Toolbars/File", position = 600)
})
@Messages("CTL_RemoveAction=Remove")
public final class RemoveAction implements ActionListener {

    private final List<RemoveCookie> context;

    public RemoveAction(List<RemoveCookie> context) {
        this.context = context;
    }

    public void actionPerformed(ActionEvent ev) {
        for (RemoveCookie removeCookie : context) {
            removeCookie.run();
        }
    }
}
