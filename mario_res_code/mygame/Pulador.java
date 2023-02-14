package mygame;

import core.ResourceManager;
import core.gameobject.Animation;
import core.gameobject.BaseObject;
import core.phisics.CollisionBox;
import core.phisics.CollisionResult;
import core.phisics.Vector2D;

//Pulador.java
public class Pulador extends BaseObject{
  //  private static final long serialVersionUID = 6529685098267757800L;
    private Animation paradoDireita;
    private Animation paradoEsquerda;
    
    private Animation andaDireita;
    private Animation andaEsquerda;
    
    private Animation pulandoDireita;
    private Animation pulandoEsquerda;
    
    
    private boolean andando;
    private boolean pulando;
    private boolean correndo;
    private boolean quicando;
    
    private double velocidadeAndar;
    
    private final double MIN_VEL_ANDAR = 200;
    private final double MAX_VEL_CORRER = 500;
    private final double INC_VEL_CORRER = 25;
    
    private final double MAX_PULO = 90;
    private double puloTotal = 0;
    private final int VELOCIDADE_PULAR = 300;
    private final double ALTURA_QUICAR = 50;
    
    
    public Pulador()
    {
        init();
    }
    
    
    public void init()
    {
        alive = true;
        System.out.println("load paradoDireita");
        paradoDireita = Animation.load(ResourceManager.createResourceString("mario_parado_dir.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        System.out.println("load andaDireita");
        andaDireita = Animation.load(ResourceManager.createResourceString("mario_anda_dir2.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        paradoEsquerda = Animation.load(ResourceManager.createResourceString("mario_parado_esq.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        andaEsquerda = Animation.load(ResourceManager.createResourceString("mario_anda_esq.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        pulandoDireita = Animation.load(ResourceManager.createResourceString("mario_pula_dir.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        pulandoEsquerda = Animation.load(ResourceManager.createResourceString("mario_pula_esq.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        currentAnimation = paradoDireita;
        currentSprite = currentAnimation.getCurrentSprite();
        velocidadeAndar = MIN_VEL_ANDAR; //200 pixels segundo.

    }
    @Override
    public void update(double tempo) {
        System.out.println("STEPP: "+(velocidadeAndar * tempo));
        
        onTopOfSomething = false;
        
       
        
        
            
        
        
	//sera utilizado para a colisao
    displacement.x = 0;
    displacement.y = 0;
    //atualiza a animacao e pega o sprite
    currentAnimation.update(tempo);
        
    
    currentSprite = currentAnimation.getCurrentSprite();
    
    
    //se esta correndo e andando, move mais rapido
    if(correndo && andando){
    	velocidadeAndar += tempo * INC_VEL_CORRER;
       	if(velocidadeAndar > MAX_VEL_CORRER)
       		velocidadeAndar = MAX_VEL_CORRER;
    }
    else if( velocidadeAndar > MIN_VEL_ANDAR){
    	velocidadeAndar -= tempo * INC_VEL_CORRER;
    }
    else
        velocidadeAndar = MIN_VEL_ANDAR;
    //se esta andando, atualiza a posicao de
    //acordo com o lado que esta 'olhando'   
    if(andando){            
        if(facingSide == FACING_RIGHT){
            double inc = velocidadeAndar * tempo;
            //se não está no final do ceário
            if(position.x+currentSprite.getImage().getWidth(null)+1+inc <mapReference.getMapWidth()){
                setNewPosition(position.x+inc, position.y);
            }
        }
        else if(facingSide == FACING_LEFT){
            double inc = velocidadeAndar * tempo;
            if(position.x-inc >0){
                setNewPosition(position.x-inc, position.y);
            }
        }
    }
    //se esta pulando ou quicando incrementa a altura
    if(pulando){
        setNewPosition(position.x, position.y+VELOCIDADE_PULAR*tempo);
        puloTotal += VELOCIDADE_PULAR*tempo;
        if(puloTotal >= MAX_PULO || onCeil){
            pulando = false;
            puloTotal = 0;
        }
    }
    else if(quicando){
        setNewPosition(position.x, position.y+VELOCIDADE_PULAR*tempo);
        puloTotal += VELOCIDADE_PULAR*tempo;
        if(puloTotal > ALTURA_QUICAR){
            puloTotal = 0;
            quicando = false;
        }
    }
    //se esta no ar aplica gravidade
    else if (!onTopOfSomething){
        apllyGravity(tempo);
        if(this.andando && facingSide == FACING_RIGHT)
            currentAnimation = andaDireita;
        else if(andando && facingSide == FACING_LEFT)
            currentAnimation = andaEsquerda;
        else if(facingSide == FACING_RIGHT)
            currentAnimation = paradoDireita;
        else if(facingSide == FACING_LEFT)
            currentAnimation = paradoEsquerda;
    }
     
    for(BaseObject b:mapReference.getObjects())
        {
            if(b.getClass().getName().equalsIgnoreCase("mygame.Plataforma")){   
                CollisionResult collType = currentSprite.getScenaryCollisionBox().centerBottomAt(position).collisionType(b.getCurrentSprite().getScenaryCollisionBox().centerBottomAt(b.getPosition()),  displacement);
                if(collType.getCollisionType()!=0)
                
                    
                if(collType.getCollisionType() == CollisionBox.COLLISION_TYPE_DOWN)
                {
                    System.out.println("DIST: "+collType.getDistance().y);
                    if(collType.getDistance().y <=7)
                        this.putOnTop(b);
                    break;
                }
            }
        }
        
}
    
    
    public void setAndando(boolean andando){
        
        this.andando = andando;
        
        /*currentAnimation = andaDireita;
        currentAnimation.restartAnimation();*/
        //System.out.println("++++++++++++++++++++++++++++++++anda dirrrrrr");
        if(this.andando){
            if(facingSide == FACING_RIGHT)
                currentAnimation = andaDireita;
            else
                currentAnimation = andaEsquerda;
        }
        else{
            if(facingSide == FACING_RIGHT)
                currentAnimation = paradoDireita;
            else
                currentAnimation = paradoEsquerda;
        }
    }
    
    public void setPulando(boolean pulando) {
        if(onFloor || onTopOfSomething){
            this.pulando = pulando;
            if(facingSide == FACING_RIGHT)
                currentAnimation = pulandoDireita;
            else 
                currentAnimation = pulandoEsquerda;
            currentAnimation.restartAnimation();           
        }
    }
    
    public void setQuicando(boolean q){
        quicando = q;
        if(q)
            puloTotal = 0;
    }
    
    
    public boolean isCorrendo() {
        return correndo;
    }

    public void setCorrendo(boolean correndo) {
        this.correndo = correndo;
    }
    
    public boolean isPulando() {
        return pulando;
    }
}
