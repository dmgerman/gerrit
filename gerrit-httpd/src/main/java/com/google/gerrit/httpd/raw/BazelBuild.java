begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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

begin_class
DECL|class|BazelBuild
specifier|public
class|class
name|BazelBuild
implements|implements
name|BuildSystem
block|{
DECL|field|sourceRoot
specifier|private
specifier|final
name|Path
name|sourceRoot
decl_stmt|;
DECL|method|BazelBuild (Path sourceRoot)
specifier|public
name|BazelBuild
parameter_list|(
name|Path
name|sourceRoot
parameter_list|)
block|{
name|this
operator|.
name|sourceRoot
operator|=
name|sourceRoot
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|build (Label l)
specifier|public
name|void
name|build
parameter_list|(
name|Label
name|l
parameter_list|)
throws|throws
name|IOException
throws|,
name|BuildFailureException
block|{
throw|throw
operator|new
name|BuildFailureException
argument_list|(
literal|"not implemented yet."
operator|.
name|getBytes
argument_list|()
argument_list|)
throw|;
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
literal|"bazel build "
operator|+
name|l
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|targetPath (Label l)
specifier|public
name|Path
name|targetPath
parameter_list|(
name|Label
name|l
parameter_list|)
block|{
return|return
name|sourceRoot
operator|.
name|resolve
argument_list|(
literal|"bazel-bin"
argument_list|)
operator|.
name|resolve
argument_list|(
name|l
operator|.
name|pkg
argument_list|)
operator|.
name|resolve
argument_list|(
name|l
operator|.
name|name
argument_list|)
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
return|return
operator|new
name|Label
argument_list|(
literal|"gerrit-gwtui"
argument_list|,
literal|"ui_"
operator|+
name|agent
operator|+
literal|".zip"
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
literal|"fonts.zip"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

