package com.eegeo.apisamples;

/**
 * A list of all the examples.
 */
public final class ExamplesList {

    public static final ExampleContainer[] Examples = {
            new ExampleContainer(R.string.basic_example_label,
                    R.string.basic_example_description,
                    BasicMapActivity.class),
            new ExampleContainer(R.string.move_camera_example_label,
                    R.string.move_camera_example_description,
                    MoveCameraActivity.class),
            new ExampleContainer(R.string.animate_camera_example_label,
                    R.string.animate_camera_example_description,
                    AnimateCameraActivity.class),
            new ExampleContainer(R.string.move_camera_to_indoor_example_label,
                    R.string.move_camera_to_indoor_example_description,
                    MoveCameraToIndoorActivity.class),
            new ExampleContainer(R.string.animate_camera_to_indoor_example_label,
                    R.string.animate_camera_to_indoor_example_description,
                    AnimateCameraToIndoorActivity.class),
            new ExampleContainer(R.string.framing_camera_example_label,
                    R.string.framing_camera_example_description,
                    FrameCameraActivity.class),
            new ExampleContainer(R.string.query_camera_example_label,
                    R.string.query_camera_example_description,
                    QueryCameraActivity.class),
            new ExampleContainer(R.string.picking_camera_example_label,
                    R.string.picking_camera_example_description,
                    PickingCameraActivity.class),
            new ExampleContainer(R.string.exit_indoor_example_label,
                    R.string.exit_indoor_example_description,
                    ExitIndoorExampleActivity.class),
            new ExampleContainer(R.string.name_indoor_example_label,
                    R.string.name_indoor_example_description,
                    NameIndoorExampleActivity.class),
            new ExampleContainer(R.string.move_floor_indoor_example_label,
                    R.string.move_floor_indoor_example_description,
                    MoveIndoorExampleActivity.class),
            new ExampleContainer(R.string.ux_floor_indoor_example_label,
                    R.string.ux_floor_indoor_example_description,
                    UXIndoorExampleActivity.class),
            new ExampleContainer(R.string.get_indoor_map_entity_information_example_label,
                    R.string.get_indoor_map_entity_information_example_description,
                    QueryIndoorMapEntityInformation.class),
            new ExampleContainer(R.string.highlight_indoor_entities_example_label,
                    R.string.highlight_indoor_entities_example_description,
                    HighlightIndoorEntityActivity.class),
            new ExampleContainer(R.string.picking_indoor_entities_example_label,
                    R.string.picking_indoor_entities_example_description,
                    PickingIndoorEntityActivity.class),
            new ExampleContainer(R.string.add_marker_example_label,
                    R.string.add_marker_example_description,
                    AddMarkerActivity.class),
            new ExampleContainer(R.string.marker_custom_icon_example_label,
                    R.string.marker_custom_icon_example_description,
                    MarkerCustomIconActivity.class),
            new ExampleContainer(R.string.marker_with_elevation_example_label,
                    R.string.marker_with_elevation_example_description,
                    MarkerWithElevationActivity.class),
            new ExampleContainer(R.string.marker_with_absolute_altitude_example_label,
                    R.string.marker_with_absolute_altitude_example_description,
                    MarkerWithAbsoluteAltitudeActivity.class),
            new ExampleContainer(R.string.add_indoor_marker_example_label,
                    R.string.add_indoor_marker_example_description,
                    AddIndoorMarkerActivity.class),
            new ExampleContainer(R.string.marker_change_text_example_label,
                    R.string.marker_change_text_example_label_description,
                    MarkerChangeTextActivity.class),
            new ExampleContainer(R.string.marker_change_location_example_label,
                    R.string.marker_change_location_example_description,
                    MarkerChangeLocationActivity.class),
            new ExampleContainer(R.string.marker_tapped_notification_example_label,
                    R.string.marker_tapped_notification_example_description,
                    MarkerTappedNotificationActivity.class),
            new ExampleContainer(R.string.marker_change_draw_order_example_label,
                    R.string.marker_change_draw_order_example_description,
                    MarkerChangeDrawOrderActivity.class),
            new ExampleContainer(R.string.position_view_on_map_example_label,
                    R.string.position_view_on_map_example_description,
                    PositionViewOnMapActivity.class),
            new ExampleContainer(R.string.position_code_created_view_on_map_example_label,
                    R.string.position_code_created_view_on_map_example_description,
                    PositionCodeCreatedViewOnMapActivity.class),
            new ExampleContainer(R.string.add_polygon_example_label,
                    R.string.add_polygon_example_description,
                    AddPolygonActivity.class),
            new ExampleContainer(R.string.add_polygon_indoors_example_label,
                    R.string.add_polygon_indoors_example_description,
                    AddPolygonIndoorsActivity.class),
            new ExampleContainer(R.string.add_polygon_with_holes_example_label,
                    R.string.add_polygon_with_holes_example_description,
                    AddPolygonWithHolesActivity.class),
            new ExampleContainer(R.string.add_polygon_with_elevation_example_label,
                    R.string.add_polygon_with_elevation_example_description,
                    AddPolygonWithElevationActivity.class),
            new ExampleContainer(R.string.add_polyline_example_label,
                    R.string.add_polyline_example_description,
                    AddPolylineActivity.class),
            new ExampleContainer(R.string.add_polyline_indoors_example_label,
                    R.string.add_polyline_indoors_example_description,
                    AddPolylineIndoorsActivity.class),
            new ExampleContainer(R.string.bluesphere_change_location_example_label,
                    R.string.bluesphere_change_location_example_description,
                    BlueSphereChangeLocationActivity.class),
            new ExampleContainer(R.string.bluesphere_change_bearing_example_label,
                    R.string.bluesphere_change_bearing_example_description,
                    BlueSphereChangeBearingActivity.class),
            new ExampleContainer(R.string.bluesphere_change_elevation_example_label,
                    R.string.bluesphere_change_elevation_example_description,
                    BlueSphereChangeElevationActivity.class),
            new ExampleContainer(R.string.bluesphere_indoors_example_label,
                    R.string.bluesphere_indoors_example_description,
                    BlueSphereIndoorsActivity.class),
            new ExampleContainer(R.string.add_building_highlight_example_label,
                    R.string.add_building_highlight_example_description,
                    AddBuildingHighlightActivity.class),
            new ExampleContainer(R.string.remove_building_highlight_example_label,
                    R.string.remove_building_highlight_example_description,
                    RemoveBuildingHighlightActivity.class),
            new ExampleContainer(R.string.place_objects_on_buildings_example_label,
                    R.string.place_objects_on_buildings_example_description,
                    PlaceObjectsOnBuildingsActivity.class),
            new ExampleContainer(R.string.picking_buildings_example_label,
                    R.string.picking_buildings_example_description,
                    PickingBuildingsActivity.class),
            new ExampleContainer(R.string.query_building_information_example_label,
                    R.string.query_building_information_example_description,
                    QueryBuildingInformationActivity.class),
            new ExampleContainer(R.string.streaming_complete_notification_example_label,
                    R.string.streaming_complete_notification_example_description,
                    StreamingCompleteNotificationActivity.class),
            new ExampleContainer(R.string.map_options_example_label,
                    R.string.map_options_example_description,
                    EegeoMapOptionsActivity.class),
            new ExampleContainer(R.string.search_example_label,
                    R.string.search_example_description,
                    SearchExampleActivity.class),
            new ExampleContainer(R.string.cancel_search_label,
                    R.string.cancel_search_description,
                    CancelSearchActivity.class),
            new ExampleContainer(R.string.load_mapscene_label,
                    R.string.load_mapscene_description,
                    LoadMapsceneActivity.class),
            new ExampleContainer(R.string.route_example_label,
                    R.string.route_example_description,
                    OutdoorRouteActivity.class),
            new ExampleContainer(R.string.indoor_route_example_label,
                    R.string.indoor_route_example_description,
                    IndoorRouteActivity.class),
            new ExampleContainer(R.string.multipart_route_example_label,
                    R.string.multipart_route_example_description,
                    MultipartRouteActivity.class),
            new ExampleContainer(R.string.cancel_route_query_label,
                    R.string.cancel_route_query_description,
                    CancelRoutingQueryActivity.class),
            new ExampleContainer(R.string.route_view_example_label,
                    R.string.route_view_example_description,
                    RouteViewActivity.class),
            new ExampleContainer(R.string.route_view_style_example_label,
                    R.string.route_view_style_example_description,
                    RouteViewStyleActivity.class),
            new ExampleContainer(R.string.route_view_markers_example_label,
                    R.string.route_view_markers_example_description,
                    RouteViewMarkersActivity.class),
            new ExampleContainer(R.string.find_point_on_path_example_label,
                    R.string.find_point_on_path_example_description,
                    FindPointOnPathActivity.class),
            new ExampleContainer(R.string.find_point_on_route_example_label,
                    R.string.find_point_on_route_example_description,
                    FindPointOnRouteActivity.class),
            new ExampleContainer(R.string.precaching_map_data_example_label,
                    R.string.precaching_map_data_example_description,
                    PrecachingMapDataActivity.class),
            new ExampleContainer(R.string.cancel_precaching_map_data_example_label,
                    R.string.cancel_precaching_map_data_example_description,
                    CancelPrecachingMapDataActivity.class),
            new ExampleContainer(R.string.set_theme_manifest_example_label,
                    R.string.set_theme_manifest_example_description,
                    SetThemeManifestActivity.class)
    };

    // Pure static class
    private ExamplesList() {
    }
}
