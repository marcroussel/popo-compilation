{
    debug (67);

    int x;
    x = 4;

    if (x == 4) {
        debug(x);
    }

    if (x != 4) {
        debug(777);
    }

    x = 6;
    debug(x);

    if (x == 4) {
        debug(1);
    }
    else {
        x = x + 9;
        debug(x);
    }

    // if sans accolades (instruction seule)
    if (x == 15) debug(1500);

    // if / else sans accolades
    if (x < 10) debug(1); else debug(2);

    // else if en chaîne (attendu : 3)
    x = 5;
    if (x == 1) {
        debug(1);
    }
    else if (x == 2) {
        debug(2);
    }
    else if (x == 5) {
        debug(3);
    }
    else {
        debug(4);
    }

    // if imbriqués (attendu : 10 puis 20)
    int y;
    y = 3;
    if (x > 0) {
        debug(10);
        if (y > 5) {
            debug(99);
        }
        else {
            debug(20);
        }
    }

    // conditions composées (attendu : 30 puis 40)
    if (x > 0 && y > 0) {
        debug(30);
    }
    if (x < 0 || y == 3) {
        debug(40);
    }

    // négation et condition constante (attendu : 50 puis 60)
    if (!(x == 0)) {
        debug(50);
    }
    if (0) {
        debug(666);
    }
    else {
        debug(60);
    }

    // modification de variable dans les branches, sans else (attendu : 8)
    if (y == 3) {
        y = y + 5;
    }
    debug(y);
    x = 15;
    // attendu : 16, 17, 18, 19, 20, 21, 0, 1, 2, 3, 4
    if (x < 20) {
        while (x < 20) {
            x = x + 1;
            debug(x);
        }

        do {
            x = x + 1;
            debug(x);
        } while (x < 20);

        for (x = 0; x < 5; x = x + 1) { // To Do : int i = 0 , i++
            debug(x);
        }
    }
}
