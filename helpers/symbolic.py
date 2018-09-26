from sympy import *
from sympy.physics.quantum import TensorProduct
from sympy.physics.quantum import Dagger

subsystem_base = [Matrix([1, 0, 0]), Matrix([0, 1, 0]), Matrix([0, 0, 1])]

base = [None] * 9
for i in range(9):
    base[i] = [None] * 9

for i in range(3):
    for j in range(3):
        base[i][j] = TensorProduct(subsystem_base[i], subsystem_base[j])

sigma = 1 / 3 * (TensorProduct(base[0][1], Dagger(base[0][1])) + TensorProduct(base[1][2], Dagger(base[1][2])) + TensorProduct(base[2][0], Dagger(base[2][0])))

v = Matrix([
    [1, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 1, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 1, 0, 0],
    [0, 1, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 1, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 1, 0],
    [0, 0, 1, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 1, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 1]
])

psi = 1 / sqrt(3) * (base[0][0] + base[1][1] + base[2][2])

alpha = Symbol("a")

rho = 2 / 7 * TensorProduct(psi, Dagger(psi)) + alpha / 7 * sigma + (5 - alpha) / 7 * v * sigma * v
