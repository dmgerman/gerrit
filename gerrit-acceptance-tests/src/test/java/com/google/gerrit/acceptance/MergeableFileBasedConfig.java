begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|storage
operator|.
name|file
operator|.
name|FileBasedConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|FS
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * A file based Config that can merge another Config instance.  */
end_comment

begin_class
DECL|class|MergeableFileBasedConfig
specifier|public
class|class
name|MergeableFileBasedConfig
extends|extends
name|FileBasedConfig
block|{
DECL|method|MergeableFileBasedConfig (File cfgLocation, FS fs)
specifier|public
name|MergeableFileBasedConfig
parameter_list|(
name|File
name|cfgLocation
parameter_list|,
name|FS
name|fs
parameter_list|)
block|{
name|super
argument_list|(
name|cfgLocation
argument_list|,
name|fs
argument_list|)
expr_stmt|;
block|}
comment|/**    * Merge another Config into this Config.    *    * In case a configuration parameter exists both in this instance and in the    * merged instance then the value in this instance will simply replaced by    * the value from the merged instance.    *    * @param s Config to merge into this instance    */
DECL|method|merge (Config s)
specifier|public
name|void
name|merge
parameter_list|(
name|Config
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|String
name|section
range|:
name|s
operator|.
name|getSections
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|subsection
range|:
name|s
operator|.
name|getSubsections
argument_list|(
name|section
argument_list|)
control|)
block|{
for|for
control|(
name|String
name|name
range|:
name|s
operator|.
name|getNames
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|)
control|)
block|{
name|setStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|s
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|name
range|:
name|s
operator|.
name|getNames
argument_list|(
name|section
argument_list|)
control|)
block|{
name|setStringList
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|name
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|s
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

