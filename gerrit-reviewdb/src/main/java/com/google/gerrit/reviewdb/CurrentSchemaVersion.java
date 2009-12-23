begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Column
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|StringKey
import|;
end_import

begin_comment
comment|/** Current version of the database schema, to facilitate live upgrades. */
end_comment

begin_class
DECL|class|CurrentSchemaVersion
specifier|public
specifier|final
class|class
name|CurrentSchemaVersion
block|{
DECL|class|Key
specifier|public
specifier|static
specifier|final
class|class
name|Key
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|VALUE
specifier|private
specifier|static
specifier|final
name|String
name|VALUE
init|=
literal|"X"
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|1
argument_list|)
DECL|field|one
specifier|protected
name|String
name|one
init|=
name|VALUE
decl_stmt|;
DECL|method|Key ()
specifier|public
name|Key
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|VALUE
return|;
block|}
annotation|@
name|Override
DECL|method|set (final String newValue)
specifier|protected
name|void
name|set
parameter_list|(
specifier|final
name|String
name|newValue
parameter_list|)
block|{
assert|assert
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|newValue
argument_list|)
assert|;
block|}
block|}
comment|/** Construct a new, unconfigured instance. */
DECL|method|create ()
specifier|public
specifier|static
name|CurrentSchemaVersion
name|create
parameter_list|()
block|{
specifier|final
name|CurrentSchemaVersion
name|r
init|=
operator|new
name|CurrentSchemaVersion
argument_list|()
decl_stmt|;
name|r
operator|.
name|singleton
operator|=
operator|new
name|CurrentSchemaVersion
operator|.
name|Key
argument_list|()
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Column
DECL|field|singleton
specifier|protected
name|Key
name|singleton
decl_stmt|;
comment|/** Current version number of the schema. */
annotation|@
name|Column
DECL|field|versionNbr
specifier|public
specifier|transient
name|int
name|versionNbr
decl_stmt|;
DECL|method|CurrentSchemaVersion ()
specifier|protected
name|CurrentSchemaVersion
parameter_list|()
block|{   }
block|}
end_class

end_unit

