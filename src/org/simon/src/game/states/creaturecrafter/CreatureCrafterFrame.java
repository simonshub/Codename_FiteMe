/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states.creaturecrafter;

import javax.swing.DefaultComboBoxModel;
import org.simon.src.game.data.gameplay.cards.CardLibrary;

/**
 *
 * @author emil.simon
 */
public class CreatureCrafterFrame extends javax.swing.JFrame {
    
    public boolean flag_next=false;
    public boolean flag_prev=false;
    public boolean flag_save=false;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frameNewPack = new javax.swing.JFrame();
        labelNewPackName = new javax.swing.JLabel();
        fieldNewPackName = new javax.swing.JTextField();
        btnNewPackConfirm = new javax.swing.JButton();
        frameSaveCreatureConfirm = new javax.swing.JFrame();
        labelNewCreatureId = new javax.swing.JLabel();
        fieldNewCreatureId = new javax.swing.JTextField();
        btnSaveCreatureConfirm = new javax.swing.JButton();
        comboCreaturePack = new javax.swing.JComboBox<>();
        btnSaveToPack = new javax.swing.JButton();
        labelSaveTo = new javax.swing.JLabel();
        labelName = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();
        sep = new javax.swing.JSeparator();
        btnNext = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        labelPortrait = new javax.swing.JLabel();
        panelPointPool = new javax.swing.JPanel();
        labelPointPoolAttack = new javax.swing.JLabel();
        labelPointPoolDefence = new javax.swing.JLabel();
        labelPointPoolAgility = new javax.swing.JLabel();
        labelPointPoolArcane = new javax.swing.JLabel();
        labelPointPoolNature = new javax.swing.JLabel();
        labelPointPoolDivine = new javax.swing.JLabel();
        labelPointPoolDeath = new javax.swing.JLabel();
        spinnerPointPoolDeath = new javax.swing.JSpinner();
        spinnerPointPoolDivine = new javax.swing.JSpinner();
        spinnerPointPoolNature = new javax.swing.JSpinner();
        spinnerPointPoolArcane = new javax.swing.JSpinner();
        spinnerPointPoolAgility = new javax.swing.JSpinner();
        spinnerPointPoolDefence = new javax.swing.JSpinner();
        spinnerPointPoolAttack = new javax.swing.JSpinner();
        btnNewPack = new javax.swing.JButton();
        panelScaleOffset = new javax.swing.JPanel();
        labelPointPoolAttack1 = new javax.swing.JLabel();
        spinnerPointPoolAttack1 = new javax.swing.JSpinner();
        labelPointPoolAttack2 = new javax.swing.JLabel();
        spinnerPointPoolAttack2 = new javax.swing.JSpinner();
        labelPointPoolAttack3 = new javax.swing.JLabel();
        spinnerPointPoolAttack3 = new javax.swing.JSpinner();

        frameNewPack.setTitle("New Card Pack");

        labelNewPackName.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelNewPackName.setText("New Pack Name:");

        fieldNewPackName.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        btnNewPackConfirm.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnNewPackConfirm.setText("Save");
        btnNewPackConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewPackConfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout frameNewPackLayout = new javax.swing.GroupLayout(frameNewPack.getContentPane());
        frameNewPack.getContentPane().setLayout(frameNewPackLayout);
        frameNewPackLayout.setHorizontalGroup(
            frameNewPackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameNewPackLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameNewPackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frameNewPackLayout.createSequentialGroup()
                        .addComponent(labelNewPackName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fieldNewPackName, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))
                    .addComponent(btnNewPackConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        frameNewPackLayout.setVerticalGroup(
            frameNewPackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameNewPackLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameNewPackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldNewPackName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNewPackName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(btnNewPackConfirm)
                .addContainerGap())
        );

        frameSaveCreatureConfirm.setTitle("Save Card");

        labelNewCreatureId.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelNewCreatureId.setText("New Creature ID:");

        fieldNewCreatureId.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        btnSaveCreatureConfirm.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnSaveCreatureConfirm.setText("Save");
        btnSaveCreatureConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCreatureConfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout frameSaveCreatureConfirmLayout = new javax.swing.GroupLayout(frameSaveCreatureConfirm.getContentPane());
        frameSaveCreatureConfirm.getContentPane().setLayout(frameSaveCreatureConfirmLayout);
        frameSaveCreatureConfirmLayout.setHorizontalGroup(
            frameSaveCreatureConfirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameSaveCreatureConfirmLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameSaveCreatureConfirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSaveCreatureConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(frameSaveCreatureConfirmLayout.createSequentialGroup()
                        .addComponent(labelNewCreatureId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fieldNewCreatureId, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)))
                .addContainerGap())
        );
        frameSaveCreatureConfirmLayout.setVerticalGroup(
            frameSaveCreatureConfirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameSaveCreatureConfirmLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameSaveCreatureConfirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldNewCreatureId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNewCreatureId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(btnSaveCreatureConfirm)
                .addContainerGap())
        );

        setTitle("Card Crafter");
        setResizable(false);

        comboCreaturePack.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        comboCreaturePack.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnSaveToPack.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnSaveToPack.setText("Save Creature to Chosen Pack");
        btnSaveToPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveToPackActionPerformed(evt);
            }
        });

        labelSaveTo.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelSaveTo.setText("Choose creature pack:");

        labelName.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelName.setText("Name: ");

        fieldName.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        btnNext.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnNext.setText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrevious.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnPrevious.setText("Previous");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        labelPortrait.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPortrait.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPortrait.setText("Portrait: 'hello/world'");

        panelPointPool.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        panelPointPool.setToolTipText("");

        labelPointPoolAttack.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolAttack.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolAttack.setText("Attack: ");

        labelPointPoolDefence.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolDefence.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolDefence.setText("Defence: ");

        labelPointPoolAgility.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolAgility.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolAgility.setText("Agility: ");

        labelPointPoolArcane.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolArcane.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolArcane.setText("Arcane: ");

        labelPointPoolNature.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolNature.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolNature.setText("Nature: ");

        labelPointPoolDivine.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolDivine.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolDivine.setText("Divine: ");

        labelPointPoolDeath.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolDeath.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolDeath.setText("Death: ");

        spinnerPointPoolDeath.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        spinnerPointPoolDeath.setPreferredSize(new java.awt.Dimension(65, 20));

        spinnerPointPoolDivine.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        spinnerPointPoolNature.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        spinnerPointPoolArcane.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        spinnerPointPoolAgility.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        spinnerPointPoolDefence.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        spinnerPointPoolAttack.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        javax.swing.GroupLayout panelPointPoolLayout = new javax.swing.GroupLayout(panelPointPool);
        panelPointPool.setLayout(panelPointPoolLayout);
        panelPointPoolLayout.setHorizontalGroup(
            panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPointPoolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelPointPoolDeath, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPointPoolDivine, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPointPoolNature, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPointPoolArcane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPointPoolAgility, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPointPoolDefence, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(labelPointPoolAttack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spinnerPointPoolAttack)
                    .addComponent(spinnerPointPoolAgility)
                    .addComponent(spinnerPointPoolDivine)
                    .addComponent(spinnerPointPoolDeath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spinnerPointPoolNature, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spinnerPointPoolArcane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spinnerPointPoolDefence, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPointPoolLayout.setVerticalGroup(
            panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPointPoolLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerPointPoolAttack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPointPoolAttack))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerPointPoolDefence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPointPoolDefence))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerPointPoolAgility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPointPoolAgility))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerPointPoolArcane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPointPoolArcane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerPointPoolNature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPointPoolNature))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerPointPoolDivine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPointPoolDivine))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPointPoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerPointPoolDeath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPointPoolDeath))
                .addContainerGap())
        );

        btnNewPack.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnNewPack.setText("New Pack");
        btnNewPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewPackActionPerformed(evt);
            }
        });

        panelScaleOffset.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        labelPointPoolAttack1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolAttack1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolAttack1.setText("Health: ");

        spinnerPointPoolAttack1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        labelPointPoolAttack2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolAttack2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolAttack2.setText("Armor: ");

        spinnerPointPoolAttack2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        labelPointPoolAttack3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        labelPointPoolAttack3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPointPoolAttack3.setText("Attack Modifier: ");

        spinnerPointPoolAttack3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        javax.swing.GroupLayout panelScaleOffsetLayout = new javax.swing.GroupLayout(panelScaleOffset);
        panelScaleOffset.setLayout(panelScaleOffsetLayout);
        panelScaleOffsetLayout.setHorizontalGroup(
            panelScaleOffsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelScaleOffsetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelScaleOffsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelScaleOffsetLayout.createSequentialGroup()
                        .addComponent(labelPointPoolAttack1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerPointPoolAttack1))
                    .addGroup(panelScaleOffsetLayout.createSequentialGroup()
                        .addComponent(labelPointPoolAttack3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerPointPoolAttack3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelScaleOffsetLayout.createSequentialGroup()
                        .addComponent(labelPointPoolAttack2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerPointPoolAttack2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 9, Short.MAX_VALUE))
        );
        panelScaleOffsetLayout.setVerticalGroup(
            panelScaleOffsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelScaleOffsetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelScaleOffsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPointPoolAttack1)
                    .addComponent(spinnerPointPoolAttack1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelScaleOffsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerPointPoolAttack2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPointPoolAttack2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelScaleOffsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerPointPoolAttack3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPointPoolAttack3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelPointPool, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelScaleOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelSaveTo)
                        .addGap(14, 14, 14)
                        .addComponent(comboCreaturePack, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNewPack, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSaveToPack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelName)
                        .addGap(18, 18, 18)
                        .addComponent(fieldName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnPrevious)
                        .addGap(18, 18, 18)
                        .addComponent(labelPortrait, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(sep))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelName))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPointPool, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelScaleOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelPortrait, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sep, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnNewPack)
                        .addComponent(btnSaveToPack)
                        .addComponent(comboCreaturePack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelSaveTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        flag_next = true;
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        flag_prev = true;
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNewPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewPackActionPerformed
        this.frameNewPack.setVisible(true);
    }//GEN-LAST:event_btnNewPackActionPerformed

    private void btnNewPackConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewPackConfirmActionPerformed
        if (!fieldNewPackName.getText().isEmpty()) {
            CardLibrary.createNewPack(fieldNewPackName.getText());
            this.frameNewPack.setVisible(false);
            this.comboCreaturePack.setModel(new DefaultComboBoxModel(CardLibrary.getLoadedCardPacks()));
            this.comboCreaturePack.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_btnNewPackConfirmActionPerformed

    private void btnSaveToPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveToPackActionPerformed
        frameSaveCreatureConfirm.setVisible(true);
    }//GEN-LAST:event_btnSaveToPackActionPerformed

    private void btnSaveCreatureConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCreatureConfirmActionPerformed
        if (!fieldNewCreatureId.getText().isEmpty()) {
            flag_save = true;
            this.frameSaveCreatureConfirm.setVisible(false);
        }
    }//GEN-LAST:event_btnSaveCreatureConfirmActionPerformed

    
    
    public CreatureCrafterFrame () {
        super();
        initComponents();
        
        this.frameNewPack.setSize(356, 128);
        this.frameSaveCreatureConfirm.setSize(356, 128);
        
        this.comboCreaturePack.setModel(new DefaultComboBoxModel(CardLibrary.getLoadedCardPacks()));
        this.comboCreaturePack.setSelectedIndex(-1);
    }
    
    
    
    public static void main(String args[]) {
        // auto-generated "look-and-feel" setting
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreatureCrafterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // end of auto-generated
        // end of auto-generated
        // end of auto-generated
        // end of auto-generated
        
        java.awt.EventQueue.invokeLater(() -> {
            new CreatureCrafterFrame().setVisible(true);
        });
    }
    
    
    
    public String getId () {
        return this.fieldNewCreatureId.getText();
    }
    
    public String getNameValue () {
        if (this.fieldName!=null) return this.fieldName.getText();
        return "";
    }
    
    public String getCurrentCreaturePackName () {
        return this.comboCreaturePack.getSelectedItem().toString();
    }
    
    public int getPointAttack () {
        return Integer.parseInt(spinnerPointPoolAttack.getValue().toString());
    }
    
    public int getPointDefence () {
        return Integer.parseInt(spinnerPointPoolDefence.getValue().toString());
    }
    
    public int getPointAgility () {
        return Integer.parseInt(spinnerPointPoolAgility.getValue().toString());
    }
    
    public int getPointDivine () {
        return Integer.parseInt(spinnerPointPoolDivine.getValue().toString());
    }
    
    public int getPointDeath () {
        return Integer.parseInt(spinnerPointPoolDeath.getValue().toString());
    }
    
    public int getPointNature () {
        return Integer.parseInt(spinnerPointPoolNature.getValue().toString());
    }
    
    public int getPointArcane () {
        return Integer.parseInt(spinnerPointPoolArcane.getValue().toString());
    }
    
    public void setIconLabelValue (String icon) {
        if (this.labelPortrait!=null) this.labelPortrait.setText(icon);
    }
            
            

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewPack;
    private javax.swing.JButton btnNewPackConfirm;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnSaveCreatureConfirm;
    private javax.swing.JButton btnSaveToPack;
    private javax.swing.JComboBox<String> comboCreaturePack;
    private javax.swing.JTextField fieldName;
    private javax.swing.JTextField fieldNewCreatureId;
    private javax.swing.JTextField fieldNewPackName;
    private javax.swing.JFrame frameNewPack;
    private javax.swing.JFrame frameSaveCreatureConfirm;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelNewCreatureId;
    private javax.swing.JLabel labelNewPackName;
    private javax.swing.JLabel labelPointPoolAgility;
    private javax.swing.JLabel labelPointPoolArcane;
    private javax.swing.JLabel labelPointPoolAttack;
    private javax.swing.JLabel labelPointPoolAttack1;
    private javax.swing.JLabel labelPointPoolAttack2;
    private javax.swing.JLabel labelPointPoolAttack3;
    private javax.swing.JLabel labelPointPoolDeath;
    private javax.swing.JLabel labelPointPoolDefence;
    private javax.swing.JLabel labelPointPoolDivine;
    private javax.swing.JLabel labelPointPoolNature;
    private javax.swing.JLabel labelPortrait;
    private javax.swing.JLabel labelSaveTo;
    private javax.swing.JPanel panelPointPool;
    private javax.swing.JPanel panelScaleOffset;
    private javax.swing.JSeparator sep;
    private javax.swing.JSpinner spinnerPointPoolAgility;
    private javax.swing.JSpinner spinnerPointPoolArcane;
    private javax.swing.JSpinner spinnerPointPoolAttack;
    private javax.swing.JSpinner spinnerPointPoolAttack1;
    private javax.swing.JSpinner spinnerPointPoolAttack2;
    private javax.swing.JSpinner spinnerPointPoolAttack3;
    private javax.swing.JSpinner spinnerPointPoolDeath;
    private javax.swing.JSpinner spinnerPointPoolDefence;
    private javax.swing.JSpinner spinnerPointPoolDivine;
    private javax.swing.JSpinner spinnerPointPoolNature;
    // End of variables declaration//GEN-END:variables
}
