import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinearProbingHashST;

class BaseballElimination {

    private LinearProbingHashST<String, Integer> t = new LinearProbingHashST<String, Integer>();
    int[] w;
    int[] l;
    int[] r;
    int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        int n = in.readInt();

        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];

        for (int i = 0; i < n; i++) {

            t.put(in.readString(), i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();

            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }

        }

    }

    // number of teams
    public int numberOfTeams() {
        return t.size();
    }

    // all teams
    public Iterable<String> teams() {
        return t.keys();
    }

    private void validateTeam(String team) {

        if (!t.contains(team))
            throw new IllegalArgumentException("team does not exist.");
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeam(team);

        return w[t.get(team)];
    }

    public int losses(String team) {
        validateTeam(team);
        return l[t.get(team)];
    }

    public int remaining(String team) {
        validateTeam(team);
        return r[t.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);

        int i = t.get(team1);
        int j = t.get(team2);

        return g[i][j];

    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);

        int x = t.get(team);

        for (int i = 0; i < g.length; i++) {

            if (w[x] + r[x] < w[i])
                return true;
        }

        // for simplicity, create team/game vertices include x but no FlowEdge to/from
        // x.
        // 0-(Vteam-1) is team vertices, Vteam-(Vgame-1) is game vertices, Vteam+Vgame
        // is source, Vteam+ Vgame +1 is target.
        int Vteam = numberOfTeams();
        int Vgame = numberOfTeams() * numberOfTeams();
        int source = Vteam + Vgame;
        int target = source + 1;

        FlowNetwork G = new FlowNetwork(Vgame + Vteam + 2);

        // add game verties
        int count = 0;
        int capSource = 0;

        for (int i = 0; i < numberOfTeams(); i++) {

            if (i != x)
                G.addEdge(new FlowEdge(i, target, w[x] + r[x] - w[i]));

            for (int j = 0; j < numberOfTeams(); j++) {

                if (i >= j | i == x | j == x)
                    continue;

                count++;
                capSource += g[i][j];

                int game = Vteam + (i + j * numberOfTeams());

                G.addEdge(new FlowEdge(source, game, g[i][j]));
                G.addEdge(new FlowEdge(game, i, Double.POSITIVE_INFINITY));
                G.addEdge(new FlowEdge(game, j, Double.POSITIVE_INFINITY));

            }
        }

        assert count == Vgame;

        // StdOut.println(G.toString());

        Double maxflow = new FordFulkerson(G, source, target).value();

        if (maxflow == capSource)
            return false;
        else
            return true;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);

        if (isEliminated(team))
            return null;

    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}