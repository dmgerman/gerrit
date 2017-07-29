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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_class
DECL|class|GerritOptions
specifier|public
class|class
name|GerritOptions
block|{
DECL|field|headless
specifier|private
specifier|final
name|boolean
name|headless
decl_stmt|;
DECL|field|slave
specifier|private
specifier|final
name|boolean
name|slave
decl_stmt|;
DECL|field|forcePolyGerritDev
specifier|private
specifier|final
name|boolean
name|forcePolyGerritDev
decl_stmt|;
DECL|method|GerritOptions (boolean headless, boolean slave, boolean forcePolyGerritDev)
specifier|public
name|GerritOptions
parameter_list|(
name|boolean
name|headless
parameter_list|,
name|boolean
name|slave
parameter_list|,
name|boolean
name|forcePolyGerritDev
parameter_list|)
block|{
name|this
operator|.
name|headless
operator|=
name|headless
expr_stmt|;
name|this
operator|.
name|slave
operator|=
name|slave
expr_stmt|;
name|this
operator|.
name|forcePolyGerritDev
operator|=
name|forcePolyGerritDev
expr_stmt|;
block|}
DECL|method|headless ()
specifier|public
name|boolean
name|headless
parameter_list|()
block|{
return|return
name|headless
return|;
block|}
DECL|method|enableMasterFeatures ()
specifier|public
name|boolean
name|enableMasterFeatures
parameter_list|()
block|{
return|return
operator|!
name|slave
return|;
block|}
DECL|method|forcePolyGerritDev ()
specifier|public
name|boolean
name|forcePolyGerritDev
parameter_list|()
block|{
return|return
operator|!
name|headless
operator|&&
name|forcePolyGerritDev
return|;
block|}
block|}
end_class

end_unit

