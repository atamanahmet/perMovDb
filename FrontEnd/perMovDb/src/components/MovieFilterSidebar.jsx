import React, { useState, useEffect } from "react";
import {
  ChevronLeft,
  ChevronRight,
  Filter,
  Star,
  Calendar,
  Users,
  Clock,
  Tag,
  Globe,
  Award,
} from "lucide-react";
import { useUser } from "../context/UserContext";

const MovieFilterSidebar = () => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [expandedSections, setExpandedSections] = useState({
    genre: true,
    year: true,
    rating: true,
    duration: false,
    language: false,
    awards: false,
  });

  const { user, filters, setFilters } = useUser();

  const toggleSection = (section) => {
    setExpandedSections((prev) => ({
      ...prev,
      [section]: !prev[section],
    }));
  };

  const handleGenreToggle = (genre) => {
    setFilters((prev) => ({
      ...prev,
      genres: prev.genres.includes(genre.id)
        ? prev.genres.filter((g) => g != genre.id)
        : [...prev.genres, genre.id],
    }));
  };

  const handleLanguageToggle = (language) => {
    setFilters((prev) => ({
      ...prev,
      languages: prev.languages.includes(language)
        ? prev.languages.filter((l) => l !== language)
        : [...prev.languages, language],
    }));
  };

  const handleAwardToggle = (award) => {
    setFilters((prev) => ({
      ...prev,
      awards: prev.awards.includes(award)
        ? prev.awards.filter((a) => a !== award)
        : [...prev.awards, award],
    }));
  };
  useEffect(() => {
    console.log(filters);
  }, [filters]);

  const genres = [
    {
      id: 28,
      name: "Action",
    },
    {
      id: 12,
      name: "Adventure",
    },
    {
      id: 16,
      name: "Animation",
    },
    {
      id: 35,
      name: "Comedy",
    },
    {
      id: 80,
      name: "Crime",
    },
    {
      id: 99,
      name: "Documentary",
    },
    {
      id: 18,
      name: "Drama",
    },
    {
      id: 10751,
      name: "Family",
    },
    {
      id: 14,
      name: "Fantasy",
    },
    {
      id: 36,
      name: "History",
    },
    {
      id: 27,
      name: "Horror",
    },
    {
      id: 10402,
      name: "Music",
    },
    {
      id: 9648,
      name: "Mystery",
    },
    {
      id: 10749,
      name: "Romance",
    },
    {
      id: 878,
      name: "Science Fiction",
    },
    {
      id: 10770,
      name: "TV Movie",
    },
    {
      id: 53,
      name: "Thriller",
    },
    {
      id: 10752,
      name: "War",
    },
    {
      id: 37,
      name: "Western",
    },
  ];

  const languages = [
    "English",
    "Spanish",
    "French",
    "German",
    "Italian",
    "Japanese",
    "Korean",
    "Mandarin",
  ];
  const awards = [
    "Oscar Winner",
    "Golden Globe",
    "BAFTA",
    "Cannes",
    "Sundance",
    "Critics Choice",
  ];

  const FilterSection = ({
    title,
    icon: Icon,
    isExpanded,
    onToggle,
    children,
  }) => (
    <div className="border-b border-amber-900 last:border-b-0">
      <button
        onClick={onToggle}
        className="w-full flex items-center justify-between p-2.5 hover:bg-amber-900 transition-colors"
      >
        <div className="flex items-center gap-3">
          <Icon size={18} className="text-amber-500" />
          {!isCollapsed && (
            <span className="font-medium text-amber-100">{title}</span>
          )}
        </div>
        {!isCollapsed && (
          <ChevronRight
            size={16}
            className={`text-amber-500 transition-transform ${
              isExpanded ? "rotate-90" : ""
            }`}
          />
        )}
      </button>
      {!isCollapsed && (
        <div
          className={`overflow-hidden transition-all duration-300 ${
            isExpanded ? "max-h-96 pb-4" : "max-h-0"
          }`}
        >
          <div className="px-4">{children}</div>
        </div>
      )}
    </div>
  );

  const RangeSlider = ({ label, min, max, value, onChange, step = 1 }) => (
    <div className="space-y-3">
      <div className="flex justify-between text-sm text-amber-500">
        <span>{label}</span>
        <span>
          {value[0]} - {value[1]}
        </span>
      </div>
      <div className="relative">
        <input
          type="range"
          min={min}
          max={max}
          value={value[0]}
          step={step}
          onChange={(e) => onChange([parseInt(e.target.value), value[1]])}
          className="absolute w-full h-2 bg-amber-600 rounded-lg appearance-none cursor-pointer slider"
        />
        <input
          type="range"
          min={min}
          max={max}
          value={value[1]}
          step={step}
          onChange={(e) => onChange([value[0], parseInt(e.target.value)])}
          className="absolute w-full h-2 bg-transparent rounded-lg appearance-none cursor-pointer slider"
        />
      </div>
    </div>
  );

  const CheckboxGroup = ({
    items,
    selectedItems,
    onToggle,
    maxHeight = "max-h-48",
    itemKey = "name",
  }) => (
    <div className={`space-y-2 overflow-y-auto ${maxHeight} custom-scrollbar`}>
      {items.map((item) => {
        const isObject = typeof item === "object" && item !== null;
        const displayValue = isObject ? item[itemKey] : item;
        const checkValue = isObject ? item.id : item;
        const keyValue = isObject ? item.id || item[itemKey] : item;

        return (
          <label
            key={keyValue}
            className="flex items-center gap-3 cursor-pointer group"
          >
            <input
              type="checkbox"
              checked={selectedItems.includes(checkValue)}
              onChange={() => onToggle(item)}
              className="w-4 h-4 text-amber-500 bg-amber-700 border-amber-600 rounded focus:ring-amber-500 focus:ring-2"
            />
            <span className="text-sm text-amber-400 group-hover:text-amber-100 transition-colors">
              {displayValue}
            </span>
          </label>
        );
      })}
    </div>
  );

  return (
    <div
      className={`bg-amber-800 h-screen transition-all duration-300 ease-in-out ${
        isCollapsed ? "w-10" : "w-60"
      } border-r border-amber-700 flex flex-col`}
    >
      {/* Header */}
      <div className="flex items-center justify-between border-b border-amber-700">
        {!isCollapsed && (
          <div className="flex items-center gap-3">
            <Filter className="text-amber-500 ml-3" size={15} />
            <h2 className="text-lg font-semibold text-white">Filters</h2>
          </div>
        )}
        <button
          onClick={() => setIsCollapsed(!isCollapsed)}
          className="hover:bg-amber-700 rounded-lg transition-colors w-15 p-2 text-center"
        >
          {isCollapsed ? (
            <ChevronRight className="text-amber-500 ml-1" size={15} />
          ) : (
            <>
              <ChevronLeft className="text-amber-500" size={15} />
              <p className="-mt-5 ml-6 text-amber-100">X</p>
            </>
          )}
        </button>
      </div>

      {/* Filters */}
      <div className="flex-1 overflow-y-auto custom-scrollbar">
        <FilterSection
          title="Genres"
          icon={Tag}
          isExpanded={expandedSections.genre}
          onToggle={() => toggleSection("genre")}
        >
          <CheckboxGroup
            items={genres}
            selectedItems={filters.genres}
            onToggle={handleGenreToggle}
          />
        </FilterSection>

        <FilterSection
          title="Release Year"
          icon={Calendar}
          isExpanded={expandedSections.year}
          onToggle={() => toggleSection("year")}
        >
          <RangeSlider
            label="Year Range"
            min={1920}
            max={2024}
            value={filters.yearRange}
            onChange={(value) =>
              setFilters((prev) => ({ ...prev, yearRange: value }))
            }
          />
        </FilterSection>

        <FilterSection
          title="Rating"
          icon={Star}
          isExpanded={expandedSections.rating}
          onToggle={() => toggleSection("rating")}
        >
          <RangeSlider
            label="Rating"
            min={0}
            max={10}
            value={filters.rating}
            onChange={(value) =>
              setFilters((prev) => ({ ...prev, rating: value }))
            }
            step={0.1}
          />
        </FilterSection>

        <FilterSection
          title="Duration"
          icon={Clock}
          isExpanded={expandedSections.duration}
          onToggle={() => toggleSection("duration")}
        >
          <RangeSlider
            label="Minutes"
            min={30}
            max={300}
            value={filters.duration}
            onChange={(value) =>
              setFilters((prev) => ({ ...prev, duration: value }))
            }
            step={5}
          />
        </FilterSection>

        <FilterSection
          title="Language"
          icon={Globe}
          isExpanded={expandedSections.language}
          onToggle={() => toggleSection("language")}
        >
          <CheckboxGroup
            items={languages}
            selectedItems={filters.languages}
            onToggle={handleLanguageToggle}
            maxHeight="max-h-32"
          />
        </FilterSection>

        <FilterSection
          title="Awards"
          icon={Award}
          isExpanded={expandedSections.awards}
          onToggle={() => toggleSection("awards")}
        >
          <CheckboxGroup
            items={awards}
            selectedItems={filters.awards}
            onToggle={handleAwardToggle}
            maxHeight="max-h-32"
          />
        </FilterSection>
      </div>

      {/* Footer */}
      {!isCollapsed && (
        <div className="p-4 border-t border-amber-800 space-y-3">
          <button className="w-full bg-amber-600 hover:bg-amber-700 text-amber-100 font-medium py-2 px-4 rounded-lg transition-colors">
            Apply Filters
          </button>
          <button
            onClick={() => {
              setFilters({
                genres: [],
                yearRange: [1990, 2024],
                rating: [0, 10],
                duration: [60, 180],
                languages: [],
                awards: [],
              });
            }}
            className="w-full bg-amber-700 hover:bg-amber-600 text-amber-300 font-medium py-2 px-4 rounded-lg transition-colors"
          >
            Clear All
          </button>
        </div>
      )}
    </div>
  );
};

export default MovieFilterSidebar;
