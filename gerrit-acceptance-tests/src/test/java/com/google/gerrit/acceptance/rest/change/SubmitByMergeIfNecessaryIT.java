begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|com.google.gerrit.acceptance.rest.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|change
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|git
operator|.
name|GitUtil
operator|.
name|checkout
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|acceptance
operator|.
name|git
operator|.
name|PushOneCommit
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
operator|.
name|SubmitType
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
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
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
name|api
operator|.
name|Git
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
name|api
operator|.
name|errors
operator|.
name|GitAPIException
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
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|SubmitByMergeIfNecessaryIT
specifier|public
class|class
name|SubmitByMergeIfNecessaryIT
extends|extends
name|AbstractSubmitByMerge
block|{
annotation|@
name|Override
DECL|method|getSubmitType ()
specifier|protected
name|SubmitType
name|getSubmitType
parameter_list|()
block|{
return|return
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
return|;
block|}
annotation|@
name|Test
DECL|method|submitWithFastForward ()
specifier|public
name|void
name|submitWithFastForward
parameter_list|()
throws|throws
name|JSchException
throws|,
name|IOException
throws|,
name|GitAPIException
block|{
name|Git
name|git
init|=
name|createProject
argument_list|()
decl_stmt|;
name|RevCommit
name|oldHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change
init|=
name|createChange
argument_list|(
name|git
argument_list|)
decl_stmt|;
name|submit
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|RevCommit
name|head
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|change
operator|.
name|getCommitId
argument_list|()
argument_list|,
name|head
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|oldHead
argument_list|,
name|head
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitMultipleChanges ()
specifier|public
name|void
name|submitMultipleChanges
parameter_list|()
throws|throws
name|JSchException
throws|,
name|IOException
throws|,
name|GitAPIException
throws|,
name|OrmException
block|{
name|Git
name|git
init|=
name|createProject
argument_list|()
decl_stmt|;
name|RevCommit
name|initialHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|checkout
argument_list|(
name|git
argument_list|,
name|initialHead
operator|.
name|getId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change2
init|=
name|createChange
argument_list|(
name|git
argument_list|,
literal|"Change 2"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|checkout
argument_list|(
name|git
argument_list|,
name|initialHead
operator|.
name|getId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change3
init|=
name|createChange
argument_list|(
name|git
argument_list|,
literal|"Change 3"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
name|checkout
argument_list|(
name|git
argument_list|,
name|initialHead
operator|.
name|getId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change4
init|=
name|createChange
argument_list|(
name|git
argument_list|,
literal|"Change 4"
argument_list|,
literal|"d"
argument_list|,
literal|"d"
argument_list|)
decl_stmt|;
name|submitStatusOnly
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|submitStatusOnly
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|submit
argument_list|(
name|change4
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RevCommit
argument_list|>
name|log
init|=
name|getRemoteLog
argument_list|()
decl_stmt|;
name|RevCommit
name|tip
init|=
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|change4
operator|.
name|getCommit
argument_list|()
operator|.
name|getShortMessage
argument_list|()
argument_list|,
name|tip
operator|.
name|getParent
argument_list|(
literal|1
argument_list|)
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|tip
operator|=
name|tip
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|change3
operator|.
name|getCommit
argument_list|()
operator|.
name|getShortMessage
argument_list|()
argument_list|,
name|tip
operator|.
name|getParent
argument_list|(
literal|1
argument_list|)
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|tip
operator|=
name|tip
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|change2
operator|.
name|getCommit
argument_list|()
operator|.
name|getShortMessage
argument_list|()
argument_list|,
name|tip
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialHead
operator|.
name|getId
argument_list|()
argument_list|,
name|tip
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

