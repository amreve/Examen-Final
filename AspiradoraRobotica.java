import java.util.Random;

public class AspiradoraRobotica {

    // Definimos el terreno como un array bidimensional
    private static final int FILAS = 14;
    private static final int COLUMNAS = 18;
    private static final char LIMPIO = '.';
    private static final char SUCIO1 = 'o';
    private static final char SUCIO2 = '0';
    private static final char SUCIO3 = 'O';
    private static final char SUCIO4 = '*';
    private static final char MUEBLE = 'M';
    private static char[][] terreno = new char[FILAS][COLUMNAS];

    // Posición de la aspiradora
    private static int posX = 0;
    private static int posY = 0;

    // Contadores
    private static int pasos = 0;
    private static int suciedadRestante;
    private static int bateria = FILAS * COLUMNAS * 5; // Capacidad de recorrer 5 veces la superficie
    private static int capacidadBolsa = 35; // Capacidad inicial de la bolsa de basura
    private static int basuraRecolectada = 0; // Contador para basura recolectada

    public static void main(String[] args) {
        inicializarTerreno();
        imprimirTerreno();

        while (suciedadRestante > 0 && bateria > 0 && capacidadBolsa > 0) {
            moverAspiradora(buscarDireccionASuciedad());
            pasos++;
            bateria--;

            if (basuraRecolectada >= 15) {
                capacidadBolsa--;
                basuraRecolectada = 0; // Vacía el recolector local
            }

            imprimirTerreno();
            System.out.println("Pasos: " + pasos);
            System.out.println("Suciedad restante: " + suciedadRestante);
            System.out.println("Batería restante: " + bateria);
            System.out.println("Capacidad de bolsa restante: " + capacidadBolsa);
        }

        if (suciedadRestante == 0) {
            System.out.println("¡Limpieza terminada en " + pasos + " pasos!");
        } else if (bateria == 0) {
            System.out.println("¡Batería agotada!");
        } else if (capacidadBolsa == 0) {
            System.out.println("¡Bolsa de basura llena!");
        }
    }

    // Inicializar el terreno con zonas limpias, sucias y muebles
    private static void inicializarTerreno() {
        Random random = new Random();
        suciedadRestante = 0;

        // Inicializar todo el terreno como limpio
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                terreno[i][j] = LIMPIO;
            }
        }

        // Colocar muebles en ubicaciones específicas
        colocarMuebles(2, 2, 4, 4); // Mueble en la esquina superior izquierda
        colocarMuebles(10, 2, 12, 4); // Mueble en la esquina inferior izquierda
        colocarMuebles(2, 12, 4, 14); // Mueble en la esquina superior derecha
        colocarMuebles(10, 12, 12, 14); // Mueble en la esquina inferior derecha
        colocarMuebles(6, 7, 8, 10); // Mueble en el centro

        // Colocar suciedad aleatoriamente en el terreno
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (terreno[i][j] == LIMPIO && random.nextBoolean()) {
                    int nivelSuciedad = random.nextInt(4) + 1;
                    switch (nivelSuciedad) {
                        case 1:
                            terreno[i][j] = SUCIO1;
                            break;
                        case 2:
                            terreno[i][j] = SUCIO2;
                            break;
                        case 3:
                            terreno[i][j] = SUCIO3;
                            break;
                        case 4:
                            terreno[i][j] = SUCIO4;
                            break;
                    }
                    suciedadRestante++;
                }
            }
        }

        // Colocar la aspiradora en una posición inicial
        terreno[posX][posY] = LIMPIO; // La aspiradora no ocupa el terreno
    }

    // Colocar muebles en una área rectangular específica
    private static void colocarMuebles(int startX, int startY, int endX, int endY) {
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                terreno[i][j] = MUEBLE;
            }
        }
    }

    // Imprimir el terreno
    private static void imprimirTerreno() {
        // Imprimir la parte superior del borde
        System.out.print("+");
        for (int j = 0; j < COLUMNAS * 3 + 1; j++) {
            System.out.print("-");
        }
        System.out.println("+");

        for (int i = 0; i < FILAS; i++) {
            System.out.print("| ");
            for (int j = 0; j < COLUMNAS; j++) {
                if (i == posX && j == posY) {
                    System.out.printf("%-3s", "(O)");
                } else {
                    System.out.printf("%-3s", terreno[i][j]);
                }
            }
            System.out.println("|");
        }

        // Imprimir la parte inferior del borde
        System.out.print("+");
        for (int j = 0; j < COLUMNAS * 3 + 1; j++) {
            System.out.print("-");
        }
        System.out.println("+");

        // Imprimir la posición de la aspiradora
        System.out.println("Posición de la aspiradora: (" + posX + ", " + posY + ")");
        System.out.println();
    }

    // Mover la aspiradora en una dirección específica
    private static void moverAspiradora(int direccion) {
        switch (direccion) {
            case 0: // Arriba
                if (posX > 0 && terreno[posX - 1][posY] != MUEBLE) posX--;
                break;
            case 1: // Abajo
                if (posX < FILAS - 1 && terreno[posX + 1][posY] != MUEBLE) posX++;
                break;
            case 2: // Izquierda
                if (posY > 0 && terreno[posX][posY - 1] != MUEBLE) posY--;
                break;
            case 3: // Derecha
                if (posY < COLUMNAS - 1 && terreno[posX][posY + 1] != MUEBLE) posY++;
                break;
        }

        // Limpiar la zona si está sucia
        if (terreno[posX][posY] == SUCIO1 || terreno[posX][posY] == SUCIO2 || terreno[posX][posY] == SUCIO3 || terreno[posX][posY] == SUCIO4) {
            switch (terreno[posX][posY]) {
                case SUCIO1:
                    terreno[posX][posY] = LIMPIO;
                    suciedadRestante--;
                    break;
                case SUCIO2:
                    terreno[posX][posY] = SUCIO1;
                    break;
                case SUCIO3:
                    terreno[posX][posY] = SUCIO2;
                    break;
                case SUCIO4:
                    terreno[posX][posY] = SUCIO3;
                    break;
            }
            basuraRecolectada++;
        }
    }

    // Buscar la dirección hacia la suciedad más cercana
    private static int buscarDireccionASuciedad() {
        int direccion = -1;
        int distanciaMinima = Integer.MAX_VALUE;

        // Buscar en las cuatro direcciones
        if (posX > 0 && terreno[posX - 1][posY] != MUEBLE) { // Arriba
            int distancia = calcularDistanciaASuciedad(posX - 1, posY);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                direccion = 0;
            }
        }
        if (posX < FILAS - 1 && terreno[posX + 1][posY] != MUEBLE) { // Abajo
            int distancia = calcularDistanciaASuciedad(posX + 1, posY);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                direccion = 1;
            }
        }
        if (posY > 0 && terreno[posX][posY - 1] != MUEBLE) { // Izquierda
            int distancia = calcularDistanciaASuciedad(posX, posY - 1);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                direccion = 2;
            }
        }
        if (posY < COLUMNAS - 1 && terreno[posX][posY + 1] != MUEBLE) { // Derecha
            int distancia = calcularDistanciaASuciedad(posX, posY + 1);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                direccion = 3;
            }
        }

        return direccion;
    }

    // Calcular la distancia a la suciedad más cercana desde una posición dada
    private static int calcularDistanciaASuciedad(int x, int y) {
        int distanciaMinima = Integer.MAX_VALUE;

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (terreno[i][j] == SUCIO1 || terreno[i][j] == SUCIO2 || terreno[i][j] == SUCIO3 || terreno[i][j] == SUCIO4) {
                    int distancia = Math.abs(i - x) + Math.abs(j - y);
                    if (distancia < distanciaMinima) {
                        distanciaMinima = distancia;
                    }
                }
            }
        }

        return distanciaMinima;
    }
}