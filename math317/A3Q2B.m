function y = solver(v0,t0, h,N,tolerance)

v = zeros(N+1,1);
t = zeros(N+1,1);

v(1) = v0;
t(1) = t0;

for i=1:N
    vtmp = v(i)-9.80665*h*sin(t(i));
    v(i+1) = newtonV(vtmp,v(i), t(i),h,tolerance);
    t(i+1) = t(i)+0.1*v(i+1)/9;
end
plot(v)
end


function r = newtonV(v0,vi,ti,h,tolerance)
r = v0;
residual = 1;
while residual > tolerance
    rold = r;
    r = r - (r-vi + 9.80665*h*sin(ti+h*r/9))/(1+h*h/9*9.80665*cos(ti+h*r/9));
    residual = abs(r-rold);
end
end