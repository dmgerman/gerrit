begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.httpd.raw
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
operator|.
name|firstNonNull
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_class
DECL|class|BuckUtils
class|class
name|BuckUtils
extends|extends
name|BuildSystem
block|{
DECL|method|BuckUtils (Path sourceRoot)
name|BuckUtils
parameter_list|(
name|Path
name|sourceRoot
parameter_list|)
block|{
name|super
argument_list|(
name|sourceRoot
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newBuildProcess (Label label)
specifier|protected
name|ProcessBuilder
name|newBuildProcess
parameter_list|(
name|Label
name|label
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|properties
init|=
name|loadBuildProperties
argument_list|(
name|sourceRoot
operator|.
name|resolve
argument_list|(
literal|"buck-out/gen/tools/buck/buck.properties"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|buck
init|=
name|firstNonNull
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"buck"
argument_list|)
argument_list|,
literal|"buck"
argument_list|)
decl_stmt|;
name|ProcessBuilder
name|proc
init|=
operator|new
name|ProcessBuilder
argument_list|(
name|buck
argument_list|,
literal|"build"
argument_list|,
name|label
operator|.
name|fullName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|properties
operator|.
name|containsKey
argument_list|(
literal|"PATH"
argument_list|)
condition|)
block|{
name|proc
operator|.
name|environment
argument_list|()
operator|.
name|put
argument_list|(
literal|"PATH"
argument_list|,
name|properties
operator|.
name|getProperty
argument_list|(
literal|"PATH"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|proc
return|;
block|}
annotation|@
name|Override
DECL|method|targetPath (Label label)
specifier|public
name|Path
name|targetPath
parameter_list|(
name|Label
name|label
parameter_list|)
block|{
return|return
name|sourceRoot
operator|.
name|resolve
argument_list|(
literal|"buck-out"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"gen"
argument_list|)
operator|.
name|resolve
argument_list|(
name|label
operator|.
name|artifact
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildCommand (Label l)
specifier|public
name|String
name|buildCommand
parameter_list|(
name|Label
name|l
parameter_list|)
block|{
return|return
literal|"buck build "
operator|+
name|l
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|gwtZipLabel (String agent)
specifier|public
name|Label
name|gwtZipLabel
parameter_list|(
name|String
name|agent
parameter_list|)
block|{
comment|// TODO(davido): instead of assuming specific Buck's internal
comment|// target directory for gwt_binary() artifacts, ask Buck for
comment|// the location of user agent permutation GWT zip, e. g.:
comment|// $ buck targets --show_output //gerrit-gwtui:ui_safari \
comment|//    | awk '{print $2}'
name|String
name|t
init|=
literal|"ui_"
operator|+
name|agent
decl_stmt|;
return|return
operator|new
name|BuildSystem
operator|.
name|Label
argument_list|(
literal|"gerrit-gwtui"
argument_list|,
name|t
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"gerrit-gwtui/__gwt_binary_%s__/%s.zip"
argument_list|,
name|t
argument_list|,
name|t
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|polygerritComponents ()
specifier|public
name|Label
name|polygerritComponents
parameter_list|()
block|{
return|return
operator|new
name|Label
argument_list|(
literal|"polygerrit-ui"
argument_list|,
literal|"polygerrit_components"
argument_list|,
literal|"polygerrit-ui/polygerrit_components/"
operator|+
literal|"polygerrit_components.bower_components.zip"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|fontZipLabel ()
specifier|public
name|Label
name|fontZipLabel
parameter_list|()
block|{
return|return
operator|new
name|Label
argument_list|(
literal|"polygerrit-ui"
argument_list|,
literal|"fonts"
argument_list|,
literal|"polygerrit-ui/fonts/fonts.zip"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|name ()
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
literal|"buck"
return|;
block|}
block|}
end_class

end_unit

