function main(alpha)
    state = 1 / 21 * [
        [2, 0, 0, 0, 2, 0, 0, 0, 2],
        [0, alpha, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 5 - alpha, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 5 - alpha, 0, 0, 0, 0, 0],
        [2, 0, 0, 0, 2, 0, 0, 0, 2],
        [0, 0, 0, 0, 0, alpha, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, alpha, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 5 - alpha, 0],
        [2, 0, 0, 0, 2, 0, 0, 0, 2]
    ];

    verify(state);
end
