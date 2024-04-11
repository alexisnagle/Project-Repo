from vedo import *
import time 

class RobotArm():
    def __init__(self, p, partLengths, Phi0):
        self.arm_location = p
        self.L1, self.L2, self.L3, self.L4, self.L5 = partLengths
        self.Phi0 = Phi0


    def RotationMatrix(self, theta, axis_name):
        c = np.cos(theta * np.pi / 180)
        s = np.sin(theta * np.pi / 180)
        
        if axis_name =='x':
            rotation_matrix = np.array([[1, 0,  0],
                                        [0, c, -s],
                                        [0, s,  c]])
        if axis_name =='y':
            rotation_matrix = np.array([[ c,  0, s],
                                        [ 0,  1, 0],
                                        [-s,  0, c]])
        elif axis_name =='z':
            rotation_matrix = np.array([[c, -s, 0],
                                        [s,  c, 0],
                                        [0,  0, 1]])
        return rotation_matrix


    def getLocalFrameMatrix(self, R_ij, t_ij):           
        # Rigid-body transformation [ R t ]
        T_ij = np.block([[R_ij,                t_ij],
                        [np.zeros((1, 3)),       1]])
        
        return T_ij


    def forward_kinematics(self, Phi):
        #	Function	implementation	goes	here

        # Matrix of Frame 1 (written w.r.t. Frame 0, which is the previous frame)
        R_01 = self.RotationMatrix(self.Phi0, axis_name = 'z')   # Rotation matrix
        t_01 = self.arm_location                             # Translation vector

        T_01 = self.getLocalFrameMatrix(R_01, t_01)         # Matrix of Frame 1 w.r.t. Frame 0 (i.e., the world frame)


        # Matrix of Frame 2 (written w.r.t. Frame 1, which is the previous frame)
        R_12 = self.RotationMatrix(Phi[0][0], axis_name = 'z')   # Rotation matrix
        t_12   = np.array([[0.0], [0.0], [self.L1]])     # Translation vector

        T_12 = self.getLocalFrameMatrix(R_12, t_12)         # Matrix of Frame 2 w.r.t. Frame 1

        # Matrix of Frame 2 w.r.t. Frame 0 (i.e., the world frame)
        T_02 = T_01 @ T_12


        # Matrix of Frame 3 (written w.r.t. Frame 2, which is the previous frame)
        R_23 = self.RotationMatrix(Phi[1][0], axis_name = 'x')   # Rotation matrix
        t_23   = np.array([[0.0], [0.0], [self.L2]])     # Translation vector

        # Matrix of Frame 3 w.r.t. Frame 2
        T_23 = self.getLocalFrameMatrix(R_23, t_23)

        # Matrix of Frame 3 w.r.t. Frame 0 (i.e., the world frame)
        T_03 = T_01 @ T_12 @ T_23


        # Matrix of Frame 4 (written w.r.t. Frame 3, which is the previous frame)
        R_34 = self.RotationMatrix(Phi[2][0], axis_name = 'x')   # Rotation matrix
        t_34   = np.array([[0.0], [0.0], [self.L3]])        # Translation vector

        # Matrix of Frame 4 w.r.t. Frame 3
        T_34 = self.getLocalFrameMatrix(R_34, t_34)

        # Matrix of Frame 4 w.r.t. Frame 0 (i.e., the world frame)
        T_04 = T_01 @ T_12 @ T_23 @ T_34

        # Matrix of Frame 5 (written w.r.t. Frame 4, which is the previous frame)
        R_45 = self.RotationMatrix(Phi[3][0], axis_name = 'z')   # Rotation matrix
        t_45   = np.array([[0.0], [0.0], [self.L4]])        # Translation vector

        # Matrix of Frame 5 w.r.t. Frame 4
        T_45 = self.getLocalFrameMatrix(R_45, t_45)

        # Matrix of Frame 5 w.r.t. Frame 0 (i.e., the world frame)
        T_05 = T_01 @ T_12 @ T_23 @ T_34 @ T_45

        # Matrix of Frame 6 (written w.r.t. Frame 5, which is the previous frame)
        R_56 = self.RotationMatrix(Phi[4][0], axis_name = 'x')   # Rotation matrix
        t_56   = np.array([[0.0], [0.0], [self.L5]])        # Translation vector

        # Matrix of Frame 5 w.r.t. Frame 4
        T_56 = self.getLocalFrameMatrix(R_56, t_56)

        # Matrix of Frame 5 w.r.t. Frame 0 (i.e., the world frame)
        T_06 = T_01 @ T_12 @ T_23 @ T_34 @ T_45 @ T_56

        return T_01, T_02, T_03, T_04, T_05, T_06
    

    def f(self, Phi):
        # Matrix of Frame 1 (written w.r.t. Frame 0, which is the previous frame)
        R_01 = self.RotationMatrix(self.Phi0, axis_name = 'z')   # Rotation matrix
        t_01 = self.arm_location                             # Translation vector

        T_01 = self.getLocalFrameMatrix(R_01, t_01)         # Matrix of Frame 1 w.r.t. Frame 0 (i.e., the world frame)

        # Matrix of Frame 2 (written w.r.t. Frame 1, which is the previous frame)
        R_12 = self.RotationMatrix(Phi[0][0], axis_name = 'z')   # Rotation matrix
        t_12   = np.array([[0.0], [0.0], [self.L1]])     # Translation vector

        T_12 = self.getLocalFrameMatrix(R_12, t_12)         # Matrix of Frame 2 w.r.t. Frame 1

        # Matrix of Frame 3 (written w.r.t. Frame 2, which is the previous frame)
        R_23 = self.RotationMatrix(Phi[1][0], axis_name = 'x')   # Rotation matrix
        t_23   = np.array([[0.0], [0.0], [self.L2]])     # Translation vector

        # Matrix of Frame 3 w.r.t. Frame 2
        T_23 = self.getLocalFrameMatrix(R_23, t_23)

        # Matrix of Frame 4 (written w.r.t. Frame 3, which is the previous frame)
        R_34 = self.RotationMatrix(Phi[2][0], axis_name = 'x')   # Rotation matrix
        t_34   = np.array([[0.0], [0.0], [self.L3]])        # Translation vector

        # Matrix of Frame 4 w.r.t. Frame 3
        T_34 = self.getLocalFrameMatrix(R_34, t_34)

        # Matrix of Frame 5 (written w.r.t. Frame 4, which is the previous frame)
        R_45 = self.RotationMatrix(Phi[3][0], axis_name = 'z')   # Rotation matrix
        t_45   = np.array([[0.0], [0.0], [self.L4]])        # Translation vector

        # Matrix of Frame 5 w.r.t. Frame 4
        T_45 = self.getLocalFrameMatrix(R_45, t_45)

        # Matrix of Frame 6 (written w.r.t. Frame 5, which is the previous frame)
        R_56 = self.RotationMatrix(Phi[4][0], axis_name = 'x')   # Rotation matrix
        t_56   = np.array([[0.0], [0.0], [self.L5]])        # Translation vector

        # Matrix of Frame 5 w.r.t. Frame 4
        T_56 = self.getLocalFrameMatrix(R_56, t_56)

        # Matrix of Frame 5 w.r.t. Frame 0 (i.e., the world frame)
        T_06 = T_01 @ T_12 @ T_23 @ T_34 @ T_45 @ T_56

        e = T_06[0:3, -1]
        return e.reshape((3, 1))


    def JacobianApprox(self, Phi, d0, d1, d2, d3, d4):
        delta0 = np.array([[d0],[0],[0],[0],[0]]) 
        delta1 = np.array([[0],[d1],[0],[0],[0]]) 
        delta2 = np.array([[0],[0],[d2],[0],[0]]) 
        delta3 = np.array([[0],[0],[0],[d3],[0]]) 
        delta4 = np.array([[0],[0],[0],[0],[d4]]) 

        curr_e = self.f(Phi)

        DeD0 = (self.f(Phi + delta0) - curr_e) /d0
        DeD1 = (self.f(Phi + delta1) - curr_e) /d1
        DeD2 = (self.f(Phi + delta2) - curr_e) /d2
        DeD3 = (self.f(Phi + delta3) - curr_e) /d3
        DeD4 = (self.f(Phi + delta4) - curr_e) /d4
        
        J = np.concatenate((DeD0, DeD1, DeD2, DeD3, DeD4), axis=1)

        return J


    def gradientDescent(self, Phi):
        # Gradient descent algorithm 
        goal = np.array([[0], [0], [100]])
        xtrace = Phi                      # Stores the trajectory in x-domain
        delta_f = goal - self.f(Phi)           # Difference between predicted and target 
        dist = np.linalg.norm(delta_f)  # Error measure (distance)
        table = []                      # Table to store the iteration results                        
        it = 0                          # Iteration count 
        # Delta f 
        
        while dist > 60:    
            delta_f = goal - self.f(Phi)
            dist = np.linalg.norm(delta_f)
            # Jacobian at x
            J = self.JacobianApprox(Phi, 0.1, 0.1, 0.1, 0.1, 0.1)
            Jinv = np.linalg.pinv(J)

            # Delta x for the Delta f using the inverse Jacobian
            delta_x_mapped = Jinv @ delta_f

            # Predicted function f(x + delta_x)
            f_predicted = self.f(Phi + delta_x_mapped)

            # Scale down the step for delta x 
            delta_x_mapped_slow = 0.05 * delta_x_mapped

            # Function value at updated scaled down location  
            f_predicted_slow = self.f(Phi + delta_x_mapped_slow)
            
            table.append([dist, goal, f_predicted_slow, Phi])

            Phi = Phi + delta_x_mapped_slow
            
            # Save iteration result  
            xtrace = np.append(xtrace, Phi, axis=1)
            it +=1   
        return xtrace, table


    def createAllFrames(self, Phi):
        ## ----------------Initialize Variables----------------
        T_01, T_02, T_03, T_04, T_05, T_06 = self.forward_kinematics(Phi)
        
        ## ----------------Frame 1----------------
        Frame1 = (Box(pos=(0, 0, 25), length=200, width=140, height=50, size=(), c='#31b1f9', alpha=1.0) +
                Box(pos=(0, 0, 25), length=160, width=180, height=50, size=(), c='#31b1f9', alpha=1.0) + 
                Cylinder(r=20, height=50, pos = (80,70,25), c="#31b1f9", alpha=1.0, axis=(0,0,1)) + 
                Cylinder(r=20, height=50, pos = (80,-70,25), c="#31b1f9", alpha=1.0, axis=(0,0,1)) + 
                Cylinder(r=20, height=50, pos = (-80,70,25), c="#31b1f9", alpha=1.0, axis=(0,0,1)) + 
                Cylinder(r=20, height=50, pos = (-80,-70,25), c="#31b1f9", alpha=1.0, axis=(0,0,1))
                )
        for i in range(30):
            Frame1 += (Box(pos=(0, 0,  50 + 0.5*i), length=200 -i, width=140-i, height=0.5, size=(), c='#31b1f9', alpha=1.0) +
                    Box(pos=(0, 0,  50 + 0.5*i), length=160 -i, width=180-i, height=0.5, size=(), c='#31b1f9', alpha=1.0) + 
                    Cylinder(r=20, height=0.5, pos = (80 -0.5*i,70 -0.5*i, 50 + 0.5*i), c="#31b1f9", alpha=1.0, axis=(0,0,1)) + 
                    Cylinder(r=20, height=0.5, pos = (80 -0.5*i ,-70 +0.5*i, 50 + 0.5*i), c="#31b1f9", alpha=1.0, axis=(0,0,1)) + 
                    Cylinder(r=20, height=0.5, pos = (-80+0.5*i,70-0.5*i, 50 + 0.5*i), c="#31b1f9", alpha=1.0, axis=(0,0,1)) + 
                    Cylinder(r=20, height=0.5, pos = (-80+0.5*i,-70+0.5*i, 50 + 0.5*i), c="#31b1f9", alpha=1.0, axis=(0,0,1))
                    )
        Frame1 += Cylinder(r=70, height=15, pos = (0,0,72.5), c="#31b1f9", alpha=1.0, axis=(0,0,1))
        Frame1 += Cylinder(r=70, height=11.5, pos = (0,0,85.25), c="#494949", alpha=1.0, axis=(0,0,1))
        # Transform the part to position it at its correct location and orientation
        Frame1.apply_transform(T_01)


        ## ----------------Frame 2----------------
        Frame2 = Cylinder(r=70, height=4, pos = (0,0,2), c="#31b1f9", alpha=1.0, axis=(0,0,1))
        for i in range(5):
            Frame2 += Cylinder(r=70-i, height=0.5, pos = (0,0,4+0.5*i), c="#31b1f9", alpha=1.0, axis=(0,0,1))

        Frame2 += Box(pos=(40, 17.5, 40),length=10,width=35,height=70,size=(),c='#31b1f9',alpha=1.0)
        Frame2 += Cylinder(r=35,height=10,pos = (40, 0, 80),c="#31b1f9",alpha=1.0,axis=(1,0,0))
        Frame2 += Box(pos=(-40, 17.5, 40),length=10,width=35,height=70,size=(),c='#31b1f9',alpha=1.0)
        Frame2 += Cylinder(r=35,height=10,pos = (-40, 0, 80),c="#31b1f9",alpha=1.0,axis=(1,0,0))

        for i in range(15):
            Frame2 += Box(pos=(45+0.5*i, 17.5, 40),length=0.5,width=35-i,height=70-i,size=(),c='#31b1f9',alpha=1.0)
            Frame2 += Cylinder(r=35-i,height=0.5,pos = (45+0.5*i, 0.5*i, 80),c="#31b1f9",alpha=1.0,axis=(1,0,0))
            Frame2 += Box(pos=(-45-0.5*i, 17.5, 40),length=0.5,width=35-i,height=70-i,size=(),c='#31b1f9',alpha=1.0)
            Frame2 += Cylinder(r=35-i,height=0.5,pos = (-45-0.5*i, 0.5*i, 80),c="#31b1f9",alpha=1.0,axis=(1,0,0))

        Frame2 += Cylinder(r=15,height=1,pos = (-55, 5, 80),c="#494949",alpha=1.0,axis=(1,0,0))
        Frame2 += Cylinder(r=15,height=1,pos = (55, 5, 80),c="#494949",alpha=1.0,axis=(1,0,0))
        # Transform the part to position it at its correct location and orientation
        Frame2.apply_transform(T_02)


        ## ----------------Frame 3----------------
        Frame3 = Cylinder(r=35,height=70,pos = (0, 0, 0),c="#494949",alpha=1.0,axis=(1,0,0))
        Frame3 += Cylinder(r=15,height=70,pos = (0,0,65),c="#b2bbd1",alpha=1.0,axis=(0,0,1))
        for i in range(60):
            Frame3 += Box(pos=(0, 0, 100+0.5*i), length=25 + 0.7*i, width=25 + 0.5*i, height=0.5, size=(), c='#31b1f9', alpha=1.0)
        for i in range(91):
            Frame3 += Box(pos=(0, 0, 130+0.5*i), length=67 + 0.3*i, width=55 + 0.25*i, height=0.5, size=(), c='#31b1f9', alpha=1.0)

        Frame3 += Cylinder(r=35,height=15,pos = (40, 0, 221),c="#31b1f9",alpha=1.0,axis=(1,0,0))
        Frame3 += Cylinder(r=35,height=15,pos = (-40, 0, 221),c="#31b1f9",alpha=1.0,axis=(1,0,0))
        Frame3 += Box(pos=(40, 0, 195),length=15,width=70,height=40,size=(),c='#31b1f9',alpha=1.0)
        Frame3 += Box(pos=(-40, 0, 195),length=15,width=70,height=40,size=(),c='#31b1f9',alpha=1.0)

        for i in range(10):
            Frame3 += Cylinder(r=35-i,height=0.5,pos = (47.5+0.5*i, 0, 221),c="#31b1f9",alpha=1.0,axis=(1,0,0))
            Frame3 += Cylinder(r=35-i,height=0.5,pos = (-47.5-0.5*i, 0, 221),c="#31b1f9",alpha=1.0,axis=(1,0,0))
        Frame3 += Cylinder(r=15,height=0.5,pos = (-52.5, 0, 221),c="#494949",alpha=1.0,axis=(1,0,0))
        Frame3 += Cylinder(r=15,height=0.5,pos = (52.5, 0, 221),c="#494949",alpha=1.0,axis=(1,0,0))

        # Transform the part to position it at its correct location and orientation
        Frame3.apply_transform(T_03)

        ## ----------------Frame 4----------------
        Frame4 = Cylinder(r=15,height=70,pos = (0, 0, 0),c="#494949",alpha=1.0,axis=(1,0,0))
        Frame4 += Box(pos=(0, 0, 17.5),length=35,width=70,height=35,size=(),c='#494949',alpha=1.0)
        Frame4 += Cylinder(r=17.5,height=35,pos = (0, 0, 52.5),c="#494949",alpha=1.0,axis=(0,0,1))
        for i in range(70):
            Frame4 += Cylinder(r=17.5-0.025*i, height=0.5, pos = (0,0,70+0.5*i), c="#494949", alpha=1.0, axis=(0,0,1))
        
        # Transform the part to position it at its correct location and orientation
        Frame4.apply_transform(T_04)

        ## ----------------Frame 5----------------
        Frame5 = Cylinder(r=15,height=60,pos = (0, 0, 0),c="#b2bbd1",alpha=1.0,axis=(0,0,1))
        for i in range(60):
            Frame5 += Box(pos=(0, 0, 30+0.5*i), length=25 + 0.5*i, width=25 + 0.3*i, height=0.5, size=(), c='#31b1f9', alpha=1.0)

        Frame5 += Box(pos=(20, 0, 80),length=15,width=40,height=40,size=(),c='#31b1f9',alpha=1.0)
        Frame5 += Box(pos=(-20, 0, 80),length=15,width=40,height=40,size=(),c='#31b1f9',alpha=1.0)
        Frame5 += Cylinder(r=20,height=15,pos = (20, 0, 100),c="#31b1f9",alpha=1.0,axis=(1,0,0))
        Frame5 += Cylinder(r=20,height=15,pos = (-20, 0, 100),c="#31b1f9",alpha=1.0,axis=(1,0,0))

        Frame5 += Cylinder(r=5,height=0.5,pos = (27.5, 0, 100),c="#b2bbd1",alpha=1.0,axis=(1,0,0))
        Frame5 += Cylinder(r=5,height=0.5,pos = (-27.5, 0, 100.5),c="#b2bbd1",alpha=1.0,axis=(1,0,0))

        # Transform the part to position it at its correct location and orientation
        Frame5.apply_transform(T_05)

        ## ----------------Frame 6----------------
        Frame6 = Cylinder(r=5,height=25,pos = (0, 0, 0),c="#494949",alpha=1.0,axis=(1,0,0))
        Frame6 += Box(pos=(0, 0, 10),length=20,width=20,height=20,size=(),c='#494949',alpha=1.0)
        Frame6 += Box(pos=(0, 0, 25),length=30,width=30,height=10,size=(),c='#494949',alpha=1.0)

        # Transform the part to position it at its correct location and orientation
        Frame6.apply_transform(T_06)

        # ----------------Return Frames----------------
        self.F1 = Frame1
        self.F2 = Frame2
        self.F3 = Frame3
        self.F4 = Frame4
        self.F5 = Frame5
        self.F6 = Frame6


L = [91.5, 80, 221, 135, 100]
startingPhi = np.array([[0], [0], [90], [0], [0]])
r = 400
Robot1 = RobotArm(np.array([[r*np.cos(0)],[r*np.sin(0)],[0]]), L, -15)
Robot1.createAllFrames(startingPhi)
trace1, table1 = Robot1.gradientDescent(startingPhi)

Robot2 = RobotArm(np.array([[r*np.cos(2*np.pi/3)],[r*np.sin(2*np.pi/3)],[0]]), L, 0)
Robot2.createAllFrames(startingPhi)
trace2, table2 = Robot2.gradientDescent(startingPhi)

Robot3 = RobotArm(np.array([[r*np.cos(4*np.pi/3)],[r*np.sin(4*np.pi/3)],[0]]), L, 70)
Robot3.createAllFrames(startingPhi)
trace3, table3 = Robot3.gradientDescent(startingPhi)


video = Video("C:/Users/alexi/Downloads/inverse_kinematics_animation.mp4", backend = 'imageio', fps = 8)
axes = Axes(xrange=(-800,800), yrange=(-800, 800), zrange=(0, 700))
plt = Plotter(bg='beige', bg2='lb', axes=10, offscreen=False, interactive=False)
plt.show(axes, viewup="z")


for i in range(trace2.shape[1]):
    Robot1.createAllFrames(np.array([[trace1[0][i]], [trace1[1][i]], [trace1[2][i]], [trace1[3][i]], [trace1[4][i]]]))
    Robot2.createAllFrames(np.array([[trace2[0][i]], [trace2[1][i]], [trace2[2][i]], [trace2[3][i]], [trace2[4][i]]]))
    Robot3.createAllFrames(np.array([[trace3[0][i]], [trace3[1][i]], [trace3[2][i]], [trace3[3][i]], [trace3[4][i]]]))

    plt.clear()
    plt += axes
    plt += Circle(r=r,pos = (0, 0, 0),c="grey",alpha=1.0)
    plt += Sphere(pos=(0, 0, 100), r=20, res=24, quads=False, c='blue', alpha=1.0)
    plt += [Robot1.F1, Robot1.F2, Robot1.F3, Robot1.F4, Robot1.F5, Robot1.F6]
    plt += [Robot2.F1, Robot2.F2, Robot2.F3, Robot2.F4, Robot2.F5, Robot2.F6]
    plt += [Robot3.F1, Robot3.F2, Robot3.F3, Robot3.F4, Robot3.F5, Robot3.F6]
    for j in range(i):
        if j > 0:
            plt += Arrow(start_pt=(table1[j-1][2][0], table1[j-1][2][1], table1[j-1][2][2]), end_pt=(table1[j][2][0], table1[j][2][1], table1[j][2][2]), s = 0.5)
            plt += Arrow(start_pt=(table2[j-1][2][0], table2[j-1][2][1], table2[j-1][2][2]), end_pt=(table2[j][2][0], table2[j][2][1], table2[j][2][2]), s = 0.5)
            plt += Arrow(start_pt=(table3[j-1][2][0], table3[j-1][2][1], table3[j-1][2][2]), end_pt=(table3[j][2][0], table3[j][2][1], table3[j][2][2]), s = 0.5)
    plt.render()    #  What is the difference between render() and show()? 
    video.add_frame()


video.close()                         # merge all the recorded frames
plt.show(axes, viewup="z").interactive().close()
