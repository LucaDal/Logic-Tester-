package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PnpTransistor extends Transistor{
    public PnpTransistor(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        super(parent, ID, x, y, sizeWidth, sizeHeight);
    }
    @Override
    public boolean hasGndConnected(int pin) {
        if (pin == pinA) {
            if (!B) {
                return isConnectedToAGnd(transistorConnectedToPinA, pin) || isConnectedToAGnd(transistorConnectedToPinC, pin);
            }
            return isConnectedToAGnd(transistorConnectedToPinA, pin);
        }
        if (pin == pinB) {
            return isConnectedToAGnd(transistorConnectedToPinB, pin);
        }
        if (!B) {//pin C
            return isConnectedToAGnd(transistorConnectedToPinC, pin) || isConnectedToAGnd(transistorConnectedToPinA, pin);
        }
        return isConnectedToAGnd(transistorConnectedToPinC, pin);
    }
    @Override
    public boolean checkIfConnectedPinAreUnderVcc(int pin) {
        if (pin == pinA) {
            if (!B) {
                return checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinA, pinA) || checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinC, pinC);
            }
            if (transistorConnectedToPinA.size() != 0) {
                return checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinA, pinA);
            } else {
                return false;
            }
        } else if (pin == pinB) {
            if (transistorConnectedToPinB.size() != 0) {
                return checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinB, pinB);
            }
            return false;
        } else if (!B) {
            return checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinC, pinC) || checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinA, pinA);
        }
        if (transistorConnectedToPinC.size() != 0) {
            return checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinC, pinC);
        } else {
            return false;
        }
    }

    @Override
    public void setGrounded(boolean state, int pin) {
        if (pinA == pin) {
            if (!this.B && AisGrounded && !state) {//parte simile per il pinB attenzione nel debug -Prof non ci faccia caso pls <3
                if (hasGndConnected(pinC)) {
                    updateHashMapToGroundOrNot(transistorConnectedToPinC, true, pinC);
                } else {
                    CisGrounded = false;
                    updateHashMapToGroundOrNot(transistorConnectedToPinC, false, pinC);
                }
            }
            AisGrounded = state;
            if (AisGrounded) {
                if (!B && !CisGrounded) {
                    CisGrounded = true;
                    updateHashMapToGroundOrNot(transistorConnectedToPinC, true, pinC);
                    setState(pinC, false);
                } else {
                    updateHashMapToGroundOrNot(transistorConnectedToPinA, true, pinA);
                }
                setState(pinA, false);
                return;
            }
            if (!hasGndConnected(pinA)) {
                updateHashMapToGroundOrNot(transistorConnectedToPinA, false, pinA);
            }
        }
        if (pin == pinB) {
            BisGrounded = state;
            updateHashMapToGroundOrNot(transistorConnectedToPinB, state, pinB);
        }
        //PARTE SPECULARE AD A
        if (pinC == pin) {
            if (!B && CisGrounded && !state) {
                if (hasGndConnected(pinA)) {
                    updateHashMapToGroundOrNot(transistorConnectedToPinA, true, pinA);
                } else {
                    AisGrounded = false;
                    updateHashMapToGroundOrNot(transistorConnectedToPinA, false, pinA);
                }
            }
            CisGrounded = state;
            if (CisGrounded) {
                if (!B && !AisGrounded) {
                    AisGrounded = true;
                    updateHashMapToGroundOrNot(transistorConnectedToPinA, true, pinA);
                    setState(pinA, false);
                } else {
                    updateHashMapToGroundOrNot(transistorConnectedToPinC, true, pinC);
                }
                setState(pinC, false);
                return;
            }//controllo se oltre al gnd che rimuovo ce ne sono altri connessi in parallelo
            if (!hasGndConnected(pinC)) {//TODO it can be faster if you wont do it again after it comes from setState
                updateHashMapToGroundOrNot(transistorConnectedToPinC, false, pinC);
            }
        }

        this.isGrounded = CisGrounded || AisGrounded || BisGrounded;
    }

    @Override
    public void setState(int pin, boolean state) {
        if (pin == pinA) {
            if (this.A != state) {
                AisUpdated = true;
                this.A = state;
                //              }
                if (!this.A && !B && C && !checkIfConnectedPinAreUnderVcc(pinC)) {
                    CisUpdated = true;
                    C = false;
                }
                if (this.A && !B && !C) {
                    CisUpdated = true;
                    C = true;
                }
            }
            if (AisGrounded && !B) {
                this.isGrounded = true;
                if (C) {
                    CisUpdated = true;
                    C = false;
                }
            }
            if (!A && !B && C) {
                AisUpdated = true;
                A = true;
            }
        } else if (pin == pinB) {
            if (B != state) {
                BisUpdated = true;
                this.B = state;
                if (!state) {
                    if (CisGrounded && !AisGrounded) {
                        setGrounded(true, pinA);
                    }
                    if (AisGrounded && !CisGrounded) {
                        setGrounded(true, pinC);
                    }
                    if (A && !C) {
                        CisUpdated = true;
                        C = true;
                    }
                    if (C && !A) {
                        AisUpdated = true;
                        A = true;
                    }
                }
                if (state) {
                    if (C && !checkIfConnectedPinAreUnderVcc(pinC)) {
                        CisUpdated = true;
                        C = false;
                    }
                    if (A && !checkIfConnectedPinAreUnderVcc(pinA)) {
                        AisUpdated = true;
                        A = false;
                    }
                    if (AisGrounded && !hasGndConnected(pinA)) {
                        setGrounded(false, pinA);
                    }
                    if (CisGrounded && !hasGndConnected(pinC)) {
                        setGrounded(false, pinC);
                    }
                    if (!AisGrounded && !A && checkIfConnectedPinAreUnderVcc(pinA)) {
                        AisUpdated = true;
                        A = true;
                    }
                    if (!CisGrounded && !C && checkIfConnectedPinAreUnderVcc(pinC)) {
                        CisUpdated = true;
                        C = true;
                    }
                }
            }
        } else if (pin == pinC) { //pin C
            if (C != state) {
                CisUpdated = true;
                this.C = state;
                if (!this.C && !B && A && !checkIfConnectedPinAreUnderVcc(pinA)) {
                    AisUpdated = true;
                    A = false;
                }
                if (C && !B && !A) {
                    AisUpdated = true;
                    A = true;
                }
            }
            if (!C && !B && A) {
                CisUpdated = true;
                C = true;
            }
            if (CisGrounded && !B) {
                this.isGrounded = true;
                if (A) {
                    AisUpdated = true;
                    A = false;
                }
            }
        }
        if (AisUpdated || BisUpdated || CisUpdated){
            update();
        }
    }

    protected void initialize() {
        BufferedImage imgb, imgbAB, imgbABC, imgbAC, imgbB, imgbBC, imgbC, imgbA;
        String path = System.getProperty("user.dir");
        try {
            imgb = ImageIO.read(new File((path + "/src/main/resources/pnp.png")));
            imgbAB = ImageIO.read(new File(path + "/src/main/resources/pnpAB.png"));
            imgbABC = ImageIO.read(new File(path + "/src/main/resources/pnpABC.png"));
            imgbAC = ImageIO.read(new File(path + "/src/main/resources/pnpAC.png"));
            imgbB = ImageIO.read(new File(path + "/src/main/resources/pnpB.png"));
            imgbBC = ImageIO.read(new File(path + "/src/main/resources/pnpBC.png"));
            imgbC = ImageIO.read(new File(path + "/src/main/resources/pnpC.png"));
            imgbA = ImageIO.read(new File(path + "/src/main/resources/pnpA.png"));
            img = imgb.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgAB = imgbAB.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgABC = imgbABC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgAC = imgbAC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgB = imgbB.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgBC = imgbBC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgC = imgbC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgA = imgbA.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

}
