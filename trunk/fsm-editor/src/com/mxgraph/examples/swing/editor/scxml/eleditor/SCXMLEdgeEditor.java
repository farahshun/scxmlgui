package com.mxgraph.examples.swing.editor.scxml.eleditor;

/*
 * TextComponentDemo.java requires one additional file:
 *   DocumentSizeFilter.java
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import com.mxgraph.examples.swing.editor.fileimportexport.SCXMLEdge;
import com.mxgraph.examples.swing.editor.scxml.SCXMLGraphEditor;
import com.mxgraph.examples.swing.editor.scxml.UndoJTextField;
import com.mxgraph.examples.swing.editor.scxml.UndoJTextPane;
import com.mxgraph.util.mxResources;

public class SCXMLEdgeEditor extends SCXMLElementEditor {

	private static final long serialVersionUID = 3563719047023065063L;
	
	private static final String undoAction="Undo"; 
	private static final String redoAction="Redo"; 
	
	private UndoJTextField eventTextPane;
	private UndoJTextField conditionTextPane;
	private UndoJTextPane exeTextPane;
	private JTabbedPane tabbedPane;
    private UndoManager undo;
    private Document doc;
    private SCXMLEdge edge;
    private JMenu editMenu;

    public SCXMLEdgeEditor(SCXMLEdge e, SCXMLGraphEditor editor) {
    	super(editor);
    	setTitle("SCXML edge editor");

        edge=e;
        //we need 3 editors:
        // one for the event
        // one for the condition
        // one for the executable content
        tabbedPane = new JTabbedPane();

        DocumentChangeListener changeListener = new DocumentChangeListener(editor);

        undo=edge.getEventUndoManager();
        doc=edge.getEventDoc();
        eventTextPane=new UndoJTextField(edge.getEvent(), doc, undo);
        if (doc==null) {
        	edge.setEventDoc(doc=eventTextPane.getDocument());
        	edge.setEventUndoManager(undo=eventTextPane.getUndoManager());
        }
        doc.addDocumentListener(changeListener);
        
        undo=edge.getConditionUndoManager();
        doc=edge.getConditionDoc();
        conditionTextPane=new UndoJTextField(edge.getCondition(), doc, undo);
        if (doc==null) {
        	edge.setConditionDoc(doc=conditionTextPane.getDocument());
        	edge.setConditionUndoManager(undo=conditionTextPane.getUndoManager());
        }
        doc.addDocumentListener(changeListener);

        undo=edge.getExeUndoManager();
        doc=edge.getExeDoc();
        exeTextPane=new UndoJTextPane(edge.getExe(), doc, undo);
        if (doc==null) {
        	edge.setExeDoc(doc=exeTextPane.getDocument());
        	edge.setExeUndoManager(undo=exeTextPane.getUndoManager());
        }
        doc.addDocumentListener(changeListener);
        
        eventTextPane.setCaretPosition(0);
        eventTextPane.setMargin(new Insets(5,5,5,5));
        conditionTextPane.setCaretPosition(0);
        conditionTextPane.setMargin(new Insets(5,5,5,5));
        exeTextPane.setCaretPosition(0);
        exeTextPane.setMargin(new Insets(5,5,5,5));

        JScrollPane scrollPane = new JScrollPane(eventTextPane);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        tabbedPane.addTab("Event", scrollPane);
        scrollPane = new JScrollPane(conditionTextPane);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        tabbedPane.addTab("Condition", scrollPane);
        scrollPane = new JScrollPane(exeTextPane);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        tabbedPane.addTab("Executable content", scrollPane);

        tabbedPane.setSelectedIndex(0);
        actions=createActionTable(tabbedPane);
        tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
              actions=createActionTable(tabbedPane);
            }
          });
        
        //Add the components.
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        //Set up the menu bar.
        //actions=createActionTable(textPane);
        editMenu=createEditMenu();
        JMenuBar mb = new JMenuBar();
        mb.add(editMenu);
        setJMenuBar(mb);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     * @param editor 
     * @param pos 
     */
    public static void createAndShowSCXMLEdgeEditor(SCXMLGraphEditor editor, SCXMLEdge edge, Point pos) {
        //Create and set up the window.
        final SCXMLEdgeEditor frame = new SCXMLEdgeEditor(edge,editor);        
        frame.showSCXMLElementEditor(pos);
    }
}