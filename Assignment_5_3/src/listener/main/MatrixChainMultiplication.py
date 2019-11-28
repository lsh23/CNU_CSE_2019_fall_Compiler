def matrixChainMultipilcation(shapes):
    n = len(shapes)
    m = [ [0 for i in range(n)] for j in range(n) ]

    for l in range(1,n):
        for i in range(n-l):
            j = i+l
            m[i][j] = 10000000000
            for k in range(i,j):
                q = m[i][k] + m[k+1][j]+shapes[i][0]*shapes[k][1]*shapes[j][1]
                #m[i][k] + m[k+1][j]하면 각각 i부터k까지의 비용 k+1부터j까지의 비용이고
                #해당 두개를 곱하는 비용이
                #shape[i][0] * shape[k][1] or shape[k+1][0] * shape[j][1] 이다.

                if q<m[i][j]:
                    m[i][j] = q

    for row in m:
        for count in row:
            print('{0:7d}'.format(count),"\t",end='')
        print("")



if __name__ == '__main__':
    shapes = []
    with open("data11_matrix_chain.txt","r",encoding='utf-8') as f:
        for line in f:
            shape = line.strip().split(",")
            shape_i = int(shape[0])
            shape_j = int(shape[1])
            shapes.append([shape_i, shape_j])
    # print(shapes)

    matrixChainMultipilcation(shapes)
