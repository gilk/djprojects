/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import org.dj.cookies.api.RunCookie;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "DJModel",
id = "org.dj.actions.RunAction")
@ActionRegistration(iconBase = "org/dj/actions/play.png",
displayName = "#CTL_RunAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 162),
    @ActionReference(path = "Toolbars/File", position = 700)
})
@Messages("CTL_RunAction=Run")
public final class RunAction implements ActionListener {

    private final List<RunCookie> context;

    public RunAction(List<RunCookie> context) {
        this.context = context;
    }

    public void actionPerformed(ActionEvent ev) {
        for (RunCookie runCookie : context) {
            runCookie.run();
        }
    }
}
