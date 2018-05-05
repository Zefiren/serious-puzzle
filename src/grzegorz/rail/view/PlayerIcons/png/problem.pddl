(define (problem problem1)
    (:domain domain1)

    (:objects
        train1 - veh
        A B tc1 tc2 - loc
        b1 b2   - block
        s1 - sigItem
    )

    (:init

        (train train1)

        (tc A)
        (tc B)
        

        (tc tc1)
        (tc tc2)

        (block b1)
        (block b2)

        
        (sigDef s1)
        

        (trackBlock A b1)
        (trackBlock tc1 b2)
        (trackBlock tc2 b2)
        (trackBlock B b2)

        (safeBlock b1)
        (safeBlock b2)

       
        ; s1    s2
        ; __ __ __ __ 
        ; A  1  2  B
        (track A tc1)
        (track tc1 tc2)
        (track tc2 B)

        
        (signal s1 A tc1 b2 DANGER)

        (at train1 A)

        (inBlock train1 b1)

        (fullBlock b1)

        (last train1 A)
    )

    (:goal
        (and
            (at train1 B)
            (not(crash train1))
        )

    )

)

