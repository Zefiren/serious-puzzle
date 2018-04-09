(define (problem problem1) 
    (:domain domain1) 
 
    (:objects 
        train0 - veh 
        tc0 tc1 tc2 tc3 - loc 
        b0 b1  - block 
        s0 - sigItem 
    ) 
 
    (:init 
        (train train0)

        (tc tc0)
        (tc tc1)
        (tc tc2)
        (tc tc3)

        (block b0)
        (block b1)

        (sigDef s0)

        (trackBlock tc0 b0)
        (trackBlock tc3 b1)
        (trackBlock tc1 b1)
        (trackBlock tc2 b1)

        (safeBlock b0)
        (safeBlock b1)

        (track tc0 tc1)
        (track tc1 tc2)
        (track tc2 tc3)

        (signal s0 tc0 tc1 b1 DANGER)

        (at train0 tc0)
        (inBlock train0 b0)
        (last train0 tc0)
        (fullBlock b0)

    )
    (:goal
        (and
            (at train0 tc3)
            (not(crash train0))
        )
    )
)