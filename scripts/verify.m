function verify(state)
    is_ppt = IsPPT(rho);
    is_separable = IsSeparable(rho);
    report = sprintf('ppt: %d; separable: %d', is_ppt, is_separable);
    disp(report);
end
