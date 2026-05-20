import "./SearchBar.css";

function SearchBar({ searchText, setSearchText }) {
    return (
        <input
            className="search-bar"
            type="text"
            placeholder="Search games..."
            value={searchText}
            onChange={(event) => setSearchText(event.target.value)}
        />
    );
}

export default SearchBar;