import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinearProbingHashST;

public class BaseballElimination {

    private class Cache {
        private final Integer id;
        private FordFulkerson ff;
        private int cap;

        private Cache(Integer id, FordFulkerson ff, Integer cap) {
            this.id = id;
            this.ff = ff;
            this.cap = cap;
        }
    }

    private final LinearProbingHashST<String, Cache> cache = new LinearProbingHashST<String, Cache>();
    private final String[] t;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        int n = in.readInt();

        t = new String[n];
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];

        for (int i = 0; i < n; i++) {

            String team = in.readString();
            cache.put(team, new Cache(i, null, 0));
            t[i] = team;
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
        return cache.size();
    }

    // all teams
    public Iterable<String> teams() {
        return cache.keys();
    }

    private void validateTeam(String team) {

        if (!cache.contains(team))
            throw new IllegalArgumentException("team does not exist.");
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeam(team);

        return w[cache.get(team).id];
    }

    public int losses(String team) {
        validateTeam(team);
        return l[cache.get(team).id];
    }

    public int remaining(String team) {
        validateTeam(team);
        return r[cache.get(team).id];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);

        int i = cache.get(team1).id;
        int j = cache.get(team2).id;

        return g[i][j];
    }

    private void setCache(String team) {

        int x = cache.get(team).id;

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
        int counter = 0;
        int cap = 0;

        for (int i = 0; i < numberOfTeams(); i++) {

            if (i != x)
                G.addEdge(new FlowEdge(i, target, Math.max(0, w[x] + r[x] - w[i])));

            for (int j = 0; j < numberOfTeams(); j++) {

                if (i >= j || i == x || j == x)
                    continue;

                counter++;
                cap += g[i][j];

                int game = Vteam + (i + j * numberOfTeams());

                G.addEdge(new FlowEdge(source, game, g[i][j]));
                G.addEdge(new FlowEdge(game, i, Double.POSITIVE_INFINITY));
                G.addEdge(new FlowEdge(game, j, Double.POSITIVE_INFINITY));

            }
        }
        assert counter == Vgame;

        cache.get(team).ff = new FordFulkerson(G, source, target);
        cache.get(team).cap = cap;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);

        int x = cache.get(team).id;
        Cache thisCache = cache.get(team);

        for (int i = 0; i < g.length; i++) {

            if (w[x] + r[x] < w[i])
                return true;
        }

        if (thisCache.ff == null)
            setCache(team);

        double maxflow = thisCache.ff.value();

        if (maxflow == thisCache.cap)
            return false;
        else
            return true;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);

        if (!isEliminated(team))
            return null;

        Cache thisCache = cache.get(team);

        if (thisCache.ff == null)
            setCache(team);

        Stack<String> out = new Stack<String>();

        for (int i = 0; i < numberOfTeams(); i++) {
            if (i != thisCache.id && thisCache.ff.inCut(i))
                out.push(t[i]);
        }
        return out;
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