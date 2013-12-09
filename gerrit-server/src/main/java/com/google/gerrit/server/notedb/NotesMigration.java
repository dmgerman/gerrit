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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
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
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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

begin_comment
comment|/**  * Holds the current state of the notes DB migration.  *<p>  * During a transitional period, different subsets of the former gwtorm DB  * functionality may be enabled on the site, possibly only for reading or  * writing.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|NotesMigration
specifier|public
class|class
name|NotesMigration
block|{
annotation|@
name|VisibleForTesting
DECL|method|allEnabled ()
specifier|static
name|NotesMigration
name|allEnabled
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"notedb"
argument_list|,
literal|null
argument_list|,
literal|"write"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|//cfg.setBoolean("notedb", "patchSetApprovals", "read", true);
return|return
operator|new
name|NotesMigration
argument_list|(
name|cfg
argument_list|)
return|;
block|}
DECL|field|write
specifier|private
specifier|final
name|boolean
name|write
decl_stmt|;
DECL|field|readPatchSetApprovals
specifier|private
specifier|final
name|boolean
name|readPatchSetApprovals
decl_stmt|;
annotation|@
name|Inject
DECL|method|NotesMigration (@erritServerConfig Config cfg)
name|NotesMigration
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|write
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"notedb"
argument_list|,
literal|null
argument_list|,
literal|"write"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|readPatchSetApprovals
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"notedb"
argument_list|,
literal|"patchSetApprovals"
argument_list|,
literal|"read"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
name|readPatchSetApprovals
argument_list|,
literal|"notedb.readPatchSetApprovals not yet supported"
argument_list|)
expr_stmt|;
block|}
DECL|method|write ()
specifier|public
name|boolean
name|write
parameter_list|()
block|{
return|return
name|write
return|;
block|}
DECL|method|readPatchSetApprovals ()
specifier|public
name|boolean
name|readPatchSetApprovals
parameter_list|()
block|{
return|return
name|readPatchSetApprovals
return|;
block|}
block|}
end_class

end_unit

